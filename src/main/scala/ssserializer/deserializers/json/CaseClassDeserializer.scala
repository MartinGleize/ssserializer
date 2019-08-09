package ssserializer.deserializers.json

import ssserializer.deserializers.{AnyDeserializer, Deserializer}

import scala.collection.mutable.ArrayBuffer
import scala.reflect.runtime.universe._

/**
 * Case class deserializer for JSON encoding. Uses an implementation of case class construction through reflection
 * by Connor Doyle, found on: https://gist.github.com/ConnorDoyle/7002426
 *
 * Does not handle inner case classes for the moment (likely won't ever support it)
 */
class CaseClassDeserializer extends Deserializer[Product, JsonReader] {

  override def deserialize(t: Type, jsonReader: JsonReader, parentDeserializer: AnyDeserializer[JsonReader]): Product = {
    val nonMethodMembers = t.members.sorted.filter(!_.isMethod)
    val orderedArgs = nonMethodMembers.map(s => s.name -> s.info)
    jsonReader.skipAfter(JsonReader.CURLY_OPEN)
    val args = new ArrayBuffer[Any]()
    for (((argName, argType), index) <- orderedArgs.zipWithIndex) {
      // ignore the name of the parameter (but we could check it in theory)
      jsonReader.readJsonName()
      // read the value
      val arg = parentDeserializer.deserialize(argType, jsonReader)
      args += arg
      // read the separating comma
      if (index < orderedArgs.size - 1)
        jsonReader.skipAfter(JsonReader.COMMA)
    }
    jsonReader.skipAfter(JsonReader.CURLY_CLOSE)
    // build the case class object with this list of arguments
    newProduct(t, args.toSeq)
  }

  override def deserialize(t: Type, jsonReader: JsonReader): Product = null // TODO: relevant exception here

  def newProduct(t: Type, args: Seq[_]): Product = {
    val productFactory = new ReflectionHelpers.CaseClassFactory[Product](t)
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

      val constructorSymbol = tpe.declaration(nme.CONSTRUCTOR)

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
      def buildWith(args: Seq[_]): T = constructorMethod(args: _*).asInstanceOf[T]

    }
  }
}
