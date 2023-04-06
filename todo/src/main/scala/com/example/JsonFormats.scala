package com.example

import com.example.types.ActionPerformed
import spray.json.{NullOptions, RootJsonFormat}

//#json-formats
import spray.json.DefaultJsonProtocol

object JsonFormats extends DefaultJsonProtocol  with NullOptions {
  // import the default encoders for primitive types (Int, String, Lists etc)

  implicit val textResponseJsonFormat: RootJsonFormat[ErrorResponse] = jsonFormat3(ErrorResponse.apply)
  implicit val generalResponseJsonFormat: RootJsonFormat[GeneralResponse[String]] = jsonFormat3(GeneralResponse.apply[String])
  implicit val userJsonFormat: RootJsonFormat[User] = jsonFormat3(User.apply)
  implicit val userGeneralResponseJsonFormat: RootJsonFormat[GeneralResponse[User]] = jsonFormat3(GeneralResponse.apply[User])
  implicit val usersJsonFormat: RootJsonFormat[Users] = jsonFormat1(Users.apply)
  implicit val usersGeneralResponseJsonFormat: RootJsonFormat[GeneralResponse[Users]] = jsonFormat3(GeneralResponse.apply[Users])

  implicit val todoJsonFormat: RootJsonFormat[Todo] = jsonFormat5(Todo.apply)
  implicit val todosJsonFormat: RootJsonFormat[Todos] = jsonFormat1(Todos.apply)
}
//#json-formats