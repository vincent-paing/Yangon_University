package aungkyawpaing.yangonuniversity.ClassModels;

import java.io.Serializable;

/**
 * Created by Vincent on 13-May-15.
 */
public class Department implements Serializable {
  private String dept_name;
  private String dept_info;
  private String dept_img;
  private String dept_location;

  public Department(String dept_name, String dept_info, String dept_imgurl, String dept_location) {
    super();
    this.dept_name = dept_name;
    this.dept_info = dept_info;
    this.dept_img = dept_imgurl;
    this.dept_location = dept_location;
  }

  public Department() {
    super();
    // TODO Auto-generated constructor stub
  }

  public String getDept_name() {
    return dept_name;
  }

  public void setDept_name(String dept_name) {
    this.dept_name = dept_name;
  }

  public String getDept_info() {
    return dept_info;
  }

  public void setDept_info(String dept_info) {
    this.dept_info = dept_info;
  }

  public String getDept_img() {
    return dept_img;
  }

  public void setDept_img(String dept_img) {
    this.dept_img = dept_img;
  }

  public String getDept_location() {
    return dept_location;
  }

  public void setDept_location(String dept_location) {
    this.dept_location = dept_location;
  }
}
