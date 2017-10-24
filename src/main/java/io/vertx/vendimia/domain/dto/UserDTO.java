package io.vertx.vendimia.domain.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject( generateConverter = true )
public class UserDTO {

  private String id;

  private String username;

  private String password;

  public UserDTO() {
  }

  public UserDTO( UserDTO other ){
    this.setId( other.getId() );
    this.setUsername( other.getUsername() );
    this.setPassword( other.getPassword() );
  }

  public UserDTO( JsonObject jsonObject ){
    UserDTOConverter.fromJson( jsonObject, this );
  }

  public JsonObject toJson(){
    JsonObject jsonObject = new JsonObject();
    UserDTOConverter.toJson( this, jsonObject );
    return jsonObject;
  }

  @Override
  public String toString() {
    return this.toJson().encodePrettily();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
