package com.vertx.vendimia.service.sensor.dto;

import com.google.gson.Gson;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.Data;

/**
 * Datos básicos del sensor.
 *
 * Observar la anotación la cual nos permitirá generar utilidades para la conversión a JsonObject y viceversa
 *
 * @author manuel
 *
 */

@DataObject(generateConverter = true)
@Data
public class SensorDTO {

	private String id;
	private String type;
	private String description;
	private Double measure;
	private Gson gson = new Gson();

	public SensorDTO(JsonObject put) {
	}

	public SensorDTO(SensorDTO other) {
		this.setId(other.getId());
		this.setDescription(other.getDescription());
		this.setType(other.getType());
		this.setMeasure(other.getMeasure());
	}

  public static void toJson(SensorDTO result, JsonObject resultObject) {
  Gson gson = new Gson();
  resultObject = JsonObject.mapFrom(gson.toJson( SensorDTO.class ));
	}
/*
	public SensorDTO(JsonObject json) {
	  SensorDTOConverter.fromJson(json, this );
		//SensorDTOConverter.fromJson(json, this);
	}

	public JsonObject toJson(SensorDTO sensorDTO ) {
	  String sensorDTOJson = gson.toJson( SensorDTO.class );
		JsonObject json = new JsonObject();
		SensorDTOConverter.toJson(this, json);
		return json;
	}

	/*
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getMeasure() {
		return measure;
	}

	public void setMeasure(Double measure) {
		this.measure = measure;
	}
*/

}
