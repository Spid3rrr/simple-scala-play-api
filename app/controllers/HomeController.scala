package controllers

import javax.inject._
import play.api._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def helloworld() = Action { implicit request: Request[AnyContent] =>
    Ok("Hello World")
  }

  def sumAandBFromRequest() = Action { implicit request: Request[AnyContent] =>
    val a = request.getQueryString("a").getOrElse("0").toInt
    val b = request.getQueryString("b").getOrElse("0").toInt
    Ok((a + b).toString)
  }
}
