package ssserializer

import ssserializer.deserializers.json.{BasketballTeam, EmployedPerson, Person, PersonWithExtraValues}

object TestObjects {

  val intTests: Seq[Int] = Seq(0, 1337, -1)
  val doubleTests: Seq[Double] = Seq(0.0, 1.337, -1.337)
  val booleanTests: Seq[Boolean] = Seq(true, false)
  val stringTests: Seq[String] = Seq(null, "", "lol", "lol\"lol", "lol\nlol")

  val arrayIntTests: Seq[Array[Int]] = Seq(
    null,
    Array(),
    Array(1, 2, 3, 4, 5)
  )
  val arrayStringTests: Seq[Array[String]] = Seq(
    null,
    Array(),
    Array(null, ""),
    Array("haha", "hoho")
  )
  val arrayAnyTests: Seq[Array[Seq[Person]]] = Seq(
    null,
    Array(),
    Array(Seq()),
    Array(Seq(null)),
    Array(Seq(Person("Mary", 42)))
  )

  val sequenceTests: Seq[Seq[Seq[String]]] = Seq(
    Seq(),
    Seq(Seq()),
    Seq(Seq("")),
    Seq(Seq("11", "12"), Seq("21"))
  )

  val listTests: Seq[List[String]] = Seq(
    null,
    List(),
    List(null),
    List(""),
    List("first", "second")
  )

  val vectorTests: Seq[Vector[Int]] = Seq(
    null,
    Vector(),
    Vector(0),
    Vector(1, 2, 3, 3, 2, 1)
  )

  val setTests: Seq[Set[Person]] = Seq(
    null,
    Set(),
    Set(Person("John", 3)),
    Set(Person("Mary", 5), Person("Mary", 5), Person("John", 11))
  )

  val mapTests: Seq[Map[String, Seq[Int]]] = Seq(
    Map(),
    Map("" -> Seq()),
    Map("key1" -> Seq(11, 12), "key2" -> Seq(21, 22, 23), "" -> null)
  )

  val caseClassTests: Seq[Seq[Person]] = Seq(
    Seq(),
    Seq(null),
    Seq(Person(null, -1)),
    Seq(Person("John", 26)),
    Seq(null, Person("Maria", 75), null, Person("Olaf", 54), Person("Bob", 40))
  )

  val caseClassMoreTests: Seq[BasketballTeam] = Seq(
    null,
    BasketballTeam("Lakers", Seq(Person("Lebron", 35), Person("AD", 24)))
  )

  val caseClassDefaultParameterTests: Seq[EmployedPerson] = Seq(
    null,
    EmployedPerson("John", false),
    EmployedPerson("Mary", true),
    EmployedPerson("Mary II", hasJob = true),
    EmployedPerson("Timmy")
  )

  val caseClassWithExtraValues: Seq[PersonWithExtraValues] = Seq(
    PersonWithExtraValues("John", 40),
    PersonWithExtraValues("Mary", 50),
  )

  val optionTests: Seq[Option[String]] = Seq(
    null,
    None,
    Some(null),
    Some("Haha")
  )

  val tuple2Tests: Seq[(Int, String)] = Seq(
    null,
    3 -> "lol",
    0 -> null,
    -3 -> null
  )

  val tuple3Tests: Seq[(String, Option[Int], Person)] = Seq(
    null,
    (null: String, null, null),
    ("", None, null),
    ("haha", Some(3), Person("John", 20))
  )

}
