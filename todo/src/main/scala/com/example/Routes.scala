package com.example

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route

import scala.concurrent.Future
import com.example.UserRegistry._
import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.AskPattern._
import akka.util.Timeout
import com.example.types.ActionPerformed

//#import-json-formats
//#user-routes-class
case class GeneralResponse[T](message: String, data: Option[T] = None, success: Boolean = true)

case class ErrorResponse(message: String, error: Option[String] = None, success: Boolean = false)

class Routes(userRegistry: ActorRef[UserRegistry.Command])(implicit val system: ActorSystem[_]) {

  //#user-routes-class

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import JsonFormats._
  //#import-json-formats

  // If ask takes more time than this to complete the request is failed
  private implicit val timeout: Timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  // User
  def getUsers(): Future[Users] = userRegistry.ask(GetUsers)

  def getUser(name: String): Future[GetUserResponse] = userRegistry.ask(GetUser(name, _))

  def createUser(user: User): Future[ActionPerformed] = userRegistry.ask(CreateUser(user, _))

  def deleteUser(name: String): Future[ActionPerformed] = userRegistry.ask(DeleteUser(name, _))

  //#all-routes
  //#users-get-post
  //#users-get-delete
  private val userRoutes: Route =
  pathPrefix("users") {
    concat(
      //#users-get-delete
      pathEnd {
        concat(
          get {
            onSuccess(getUsers()) { res =>
              complete((StatusCodes.OK, GeneralResponse("success", Option(res))))
            }
          },
          post {
            entity(as[User]) { user =>
              onSuccess(createUser(user)) { performed =>
                if (performed.success) complete((StatusCodes.Created, GeneralResponse("success", Option(performed.description))))
                else complete((StatusCodes.OK, ErrorResponse("unsuccessful", Option(performed.description))))
              }
            }
          })
      },
      //#users-get-delete
      //#users-get-post
      path(Segment) { name =>
        concat(
          get {
            //#retrieve-user-info
            rejectEmptyResponse {
              onSuccess(getUser(name)) { response =>
                if (response.maybeUser.isEmpty) complete((StatusCodes.NotFound, ErrorResponse("unsuccessful", Option("User not found"))))
                else complete((StatusCodes.OK, GeneralResponse("success", Option(response.maybeUser.get))))
              }
            }
            //#retrieve-user-info
          },
          delete {
            //#users-delete-logic
            onSuccess(deleteUser(name)) { performed =>
              if (performed.success) complete((StatusCodes.OK, GeneralResponse("success", Option(performed.description))))
              else complete((StatusCodes.NotFound, ErrorResponse("unsuccessful", Option(performed.description))))
            }
            //#users-delete-logic
          })
      })
    //#users-get-delete
  }

  private val todoRoutes: Route =
    pathPrefix("todos") {
      concat(
        get {
          complete("todos")
        },
        post {
          complete("todos")
        }
      )
    }

  val routes: Route = pathPrefix("api") {
    concat(
      userRoutes,
      todoRoutes,
    )
  }
  //#all-routes
}
