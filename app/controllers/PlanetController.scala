package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.collection.mutable.ListBuffer



/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class PlanetController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

    
    case class Planet(name: String, distance: Double)

    val planets = ListBuffer(
        Planet("Mercury", 57.9),
        Planet("Venus", 108.2),
        Planet("Earth", 149.6),
        Planet("Mars", 227.9),
        Planet("Jupiter", 778.3),
        Planet("Saturn", 1427.0),
        Planet("Uranus", 2871.0),
        Planet("Neptune", 4497.1)
    )

    implicit val planetWrites: Writes[Planet] = (
    (JsPath \ "name").write[String] and
    (JsPath \ "distance").write[Double]
  )(unlift(Planet.unapply))

  implicit val planetReads: Reads[Planet] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "distance").read[Double]
  )(Planet.apply _)
    

  def getAllPlanets() = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(planets))
  }

  def getPlanet() = Action { implicit request: Request[AnyContent] =>
    val name = request.getQueryString("name").getOrElse("")
    val planet = planets.find(_.name == name)
    planet match {
      case Some(p) => Ok(Json.toJson(p))
      case None => NotFound("Planet not found")
    }
  }
  
  def getPlanetByName(name: String) = Action { implicit request: Request[AnyContent] =>
    val planet = planets.find(_.name == name)
    planet match {
      case Some(p) => Ok(Json.toJson(p))
      case None => NotFound("Planet not found")
    }
  }

  def addPlanet() = Action { implicit request: Request[AnyContent] =>
    request.body.asJson.map { json =>
          json.validate[Planet].fold(
            errors => {
              BadRequest(Json.obj("message" -> "Invalid input"))
            },
            planet => {
              planets += planet
              Created(Json.toJson(planet))
            }
          )
        }.getOrElse {
          BadRequest(Json.obj("message" -> "Expecting JSON data"))
        }
  }

  def deletePlanetByName(name: String) = Action { implicit request: Request[AnyContent] =>
    val planet = planets.find(_.name == name)
    planet match {
      case Some(p) => {
        planets -= p
        Ok(Json.obj("message" -> "Planet deleted"))
      }
      case None => NotFound("Planet not found")
    }
  }
}
