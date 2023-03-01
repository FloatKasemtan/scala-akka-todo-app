package com.example

import com.example.types.ActionPerformed
import spray.json.RootJsonFormat

//#json-formats
import spray.json.DefaultJsonProtocol

object JsonFormats  {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val userJsonFormat: RootJsonFormat[User] = jsonFormat3(User.apply)
  implicit val usersJsonFormat: RootJsonFormat[Users] = jsonFormat1(Users.apply)

  implicit val todoJsonFormat: RootJsonFormat[Todo] = jsonFormat5(Todo.apply)
  implicit val todosJsonFormat: RootJsonFormat[Todos] = jsonFormat1(Todos.apply)

  implicit val actionPerformedJsonFormat: RootJsonFormat[ActionPerformed] = jsonFormat1(ActionPerformed.apply)
}
//#json-formats