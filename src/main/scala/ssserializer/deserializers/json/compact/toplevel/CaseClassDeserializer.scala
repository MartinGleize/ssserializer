package ssserializer.deserializers.json.compact.toplevel

import ssserializer.deserializers.json.compact.JsonReaderWithMemory

import scala.reflect.runtime.universe._

class CaseClassDeserializer extends ssserializer.deserializers.json.CaseClassDeserializer[JsonReaderWithMemory] {

  override def newProduct(t: Type, args: Seq[_], jsonReader: JsonReaderWithMemory): Product = {
    // at this point the args can be either value types or Int masquerading as ref types
    // get the product factory as usual
    val productFactory = cacheConstructor.getOrElseUpdate(t, new ReflectionHelpers.CaseClassFactory[Product](t))
    // deserialize the real args (potentially going ahead in the JSON stream)
    val realArgs = jsonReader.topLevelArrayDeserializer.deserializeRefDependencies(args, jsonReader)
    // build the definitive object with all its dependencies resolved
    val res = productFactory.buildWith(realArgs)
    res
  }

}
