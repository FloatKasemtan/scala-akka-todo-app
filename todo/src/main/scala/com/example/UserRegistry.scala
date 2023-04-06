package com.example

//#user-registry-actor
import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.example.types.ActionPerformed

import scala.collection.immutable



//#user-case-classes
final case class User(name: String, age: Int, countryOfResidence: String)
final case class Users(users: immutable.Seq[User])
//#user-case-classes

object UserRegistry {
  // actor protocol
  sealed trait Command
  final case class GetUsers(replyTo: ActorRef[Users]) extends Command
  final case class CreateUser(user: User, replyTo: ActorRef[ActionPerformed]) extends Command
  final case class GetUser(name: String, replyTo: ActorRef[GetUserResponse]) extends Command
  final case class DeleteUser(name: String, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetUserResponse(maybeUser: Option[User])
//  final case class ActionPerformed(description: String)


  def apply(): Behavior[Command] = {
    println("UserRegistry apply")
    registry(Array.empty)
  }

  private def registry(users: Array[User]): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetUsers(replyTo) =>
        replyTo ! Users(users.toSeq)
        Behaviors.same
      case CreateUser(user, replyTo) =>
        if (users.exists(_.name == user.name)) {
          replyTo ! ActionPerformed(s"User ${user.name} already exists.", success = false)
          Behaviors.same
        }
        else {
          replyTo ! ActionPerformed(s"User ${user.name} created.")
          registry(users :+ user)
        }

      case GetUser(name, replyTo) =>
        replyTo ! GetUserResponse(users.find(_.name == name))
        Behaviors.same
      case DeleteUser(name, replyTo) =>
        if (!users.exists(_.name == name)) {
          replyTo ! ActionPerformed(s"User $name does not exist.", success = false)
          Behaviors.same
        }
        else {
          replyTo ! ActionPerformed(s"User $name deleted.")
          registry(users.filterNot(_.name == name))
        }
    }
}
//#user-registry-actor
