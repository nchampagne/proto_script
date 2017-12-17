package com.curious.util

import scala.concurrent.Future

trait MonadT[T] {
  protected def self: T
  def map[R](block: T => R): R = block(self)
  def flatMap[R <: MonadT[R]](block: T => R): R = block(self)
}

trait FutureAssist {
//  val logger = Logger(this.getClass())

  def asFuture[T](f: => T) = Future.successful(f)

//  def withRecovery[T](error: ErrorI)(fT: => Future[Either[ErrorI, T]]) =
//    fT.recover {
//      case NonFatal(e) =>
//        logger.error(error.error, e)
//        Left(error)
//    }
//
//  def withRecovery[T](fT: => Future[Either[ErrorI, T]]): Future[Either[ErrorI, T]] =
//    withRecovery(CommonErrors.InternalServerError)(fT)
}