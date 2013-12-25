package croudia4j;

public class CroudiaFactory {
  private String key;
  public CroudiaFactory(Configuration conf){
    this.key=conf.getKey();
  }
  
  public Croudia getInstance(){
    return new Croudia(this.key);
  }
}
