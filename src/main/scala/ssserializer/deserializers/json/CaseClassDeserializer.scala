package ssserializer.deserializers.json

import java.util

import scala.collection.JavaConversions._
import ssserializer.deserializers.{DeserializationException, MasterDeserializer}
import ssserializer.serializers.generic.CaseClassSerializer
import ssserializer.serializers.json.CaseClassSerializer

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.reflect.runtime.universe._

/**
 * Case class deserializer for JSON encoding. Uses an implementation of case class construction through reflection
 * by Connor Doyle, found on: https://gist.github.com/ConnorDoyle/7002426
 *
 * Does not handle inner case classes for the moment (likely won't ever support it)
 */
class CaseClassDeserializer extends Deserializer[Product] {

  /** Cache for the parameters of case class types */
  private val cacheParameters: mutable.Map[Type, Seq[(String, Type)]] = new util.IdentityHashMap[Type, Seq[(String, Type)]]()
  /** Cache for the reflected constructor of case class types */
  private val cacheConstructor: mutable.Map[Type, ReflectionHelpers.CaseClassFactory[Product]] =
    new util.IdentityHashMap[Type, ReflectionHelpers.CaseClassFactory[Product]]()

  override def deserializeNonNull(t: Type, jsonReader: parsing.JsonReader, parentDeserializer: MasterDeserializer[parsing.JsonReader]): Product = {
    val orderedArgs = cacheParameters.getOrElseUpdate(t, {
      val parameterSymbols = CaseClassSerializer.parameterSymbols(t)
      parameterSymbols.map(s => s.name.toString -> s.info)
    })
    jsonReader.skipAfter(parsing.JsonReader.CURLY_OPEN)
    val args = new ArrayBuffer[Any]()
    for (((argName, argType), index) <- orderedArgs.zipWithIndex) {
      // ignore the name of the parameter (but we could check it in theory)
      jsonReader.readJsonName()
      // read the value
      val arg = parentDeserializer.deserialize(argType, jsonReader)
      args += arg
      // read the separating comma
      if (index < orderedArgs.size - 1)
        jsonReader.skipAfter(parsing.JsonReader.COMMA)
    }
    jsonReader.skipAfter(parsing.JsonReader.CURLY_CLOSE)
    // build the case class object with this list of arguments
    newProduct(t, args)
  }

  def newProduct(t: Type, args: Seq[_]): Product = {
    val productFactory = cacheConstructor.getOrElseUpdate(t, new ReflectionHelpers.CaseClassFactory[Product](t))
    val res = productFactory.buildWith(args)
    res
  }

  object ReflectionHelpers extends ReflectionHelpers

  trait ReflectionHelpers {

    protected val classLoaderMirror = runtimeMirror(getClass.getClassLoader)

    /**
     * Encapsulates functionality to reflectively invoke the constructor
     * for a given case class type `T`.
     *
     * @tparam T the type of the case class this factory builds
     */
    class CaseClassFactory[T](tpe: Type) {

      val classSymbol = tpe.typeSymbol.asClass

      if (!(tpe <:< typeOf[Product] && classSymbol.isCaseClass))
        throw new IllegalArgumentException(
          "CaseClassFactory only applies to case classes!"
        )

      // TODO: this is where an exception is thrown when this deserializer is applied to an inner case class
      // TODO: handle the exception and rethrow some relevant helpful error message
      val classMirror = classLoaderMirror reflectClass classSymbol

      val constructorSymbol = tpe.decl(termNames.CONSTRUCTOR)

      val defaultConstructor =
        if (constructorSymbol.isMethod) constructorSymbol.asMethod
        else {
          val ctors = constructorSymbol.asTerm.alternatives
          ctors.map { _.asMethod }.find { _.isPrimaryConstructor }.get
        }

      val constructorMethod = classMirror reflectConstructor defaultConstructor

      /**
       * Attempts to create a new instance of the specified type by calling the
       * constructor method with the supplied arguments.
       *
       * @param args the arguments to supply to the constructor method
       */
      def buildWith(args: Seq[_]): T = {
        try {
          constructorMethod(args: _*).asInstanceOf[T]
        } catch {
          case iae: IllegalArgumentException => {
            val expectedParameterLists = defaultConstructor.paramLists.toString
            val actualArguments = args.toString()
            throw new DeserializationException(
              "Expected one of these lists of parameters:" + "\n" +
              expectedParameterLists + "\n" +
              "Got these parameters instead:" + "\n" +
              actualArguments
              , tpe)
          }
        }
      }

    }
  }
}
