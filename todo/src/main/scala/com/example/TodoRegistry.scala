package com.example

import akka.actor.typed.ActorRef
import com.example.types.ActionPerformed

import scala.collection.immutable

final case class Todo(id: Int, title: String, completed: Boolean, order: Option[Int], url: String)
final case class Todos(todos: immutable.Seq[Todo])
object TodoRegistry {
  sealed trait Command

  final case class GetTodos(replyTo: ActorRef[Todos]) extends Command
  final case class GetTodo(id: String, replyTo: ActorRef[Option[Todo]]) extends Command
  final case class CreateTodo(todo: Todo, replyTo: ActorRef[ActionPerformed]) extends Command
  final case class DeleteTodo(id: String, replyTo: ActorRef[ActionPerformed]) extends Command



}
