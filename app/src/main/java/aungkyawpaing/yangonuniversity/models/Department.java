package aungkyawpaing.yangonuniversity.models;

import java.io.Serializable;

/**
 * Created by Vincent on 13-May-15.
 */
public class Department implements Serializable {
  public int id;
  public String name;
  public String info;
  public String image;
  public String location;

  public Department() {
    super();
    // TODO Auto-generated constructor stub
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public void setLocation(String location) {
    this.location = location;
  }
}
