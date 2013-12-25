package croudia4j;

public class CroudiaFactory {
  private String key;
  private String secret;
  public CroudiaFactory(Configuration conf){
    this.key=conf.getKey();
    this.secret=conf.getSecret();
  }
  
  public Croudia getInstance(){
    return new Croudia(this.key,this.secret);
  }
}
