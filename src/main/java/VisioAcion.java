/**
 * Created by opanahi on 12/20/2016.
 */
public class VisioAcion {

     private  String NoLoop;
     private  String StandardLoop;
     private  String NoTaskType;
     private  String Service;

    public VisioAcion() {
    }

    public  String getNoLoop() {
        return NoLoop;
    }

    public  String getStandardLoop() {
        return StandardLoop;
    }

    public  String getService() {
        return Service;
    }

    public  String getNoTaskType() {
         return NoTaskType ;
    }

    @Override
    public String toString() {
        return "VisioAcion{}";
    }
   public boolean isNoloop(){
       return (this.getNoLoop().equals("Noloop"));
   }

    public  void setNoLoop(String noLoop) {
        NoLoop = noLoop;
    }

    public  void setStandardLoop(String standardLoop) {
        StandardLoop = standardLoop;
    }

    public  void setNoTaskType(String noTaskType) {
        NoTaskType = noTaskType;
    }

    public  void setService(String service) {
        Service = service;
    }

    boolean  isStandardLoop(){
        return StandardLoop.equals("StandardLoop");
    }
    boolean isNoTaskType(){
        return NoTaskType.equals("NoTaskType");
    }
    boolean isService(){
        return Service.equals("Service");
    }

  public void setValue(String string){
      switch (string){
          case "NoLoop": {
              this.NoLoop="NoLoop";
              break;
          }
          case "StandardLoop": {
              this.StandardLoop="StandardLoop";
              break;
          }
          case "NoTaskType": {
              this.NoTaskType="NoTaskType";
              break;
          }
          case "Service": {
              this.Service="Service";
              break;
          }

      }
  }
}//end of class
