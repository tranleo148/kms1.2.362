package constants;

public class JobConstants {
  public static final boolean enableJobs = true;
  
  public static final int jobOrder = 35;
  
  public enum LoginJob {
    Resistance(0, JobFlag.ENABLED),
    Adventurer(1, JobFlag.ENABLED),
    Cygnus(2, JobFlag.ENABLED),
    Aran(3, JobFlag.ENABLED),
    Evan(4, JobFlag.ENABLED),
    Mercedes(5, JobFlag.ENABLED),
    Demon(6, JobFlag.ENABLED),
    Phantom(7, JobFlag.ENABLED),
    DualBlade(8, JobFlag.ENABLED),
    Mihile(9, JobFlag.ENABLED),
    Luminous(10, JobFlag.ENABLED),
    Kaiser(11, JobFlag.ENABLED),
    AngelicBuster(12, JobFlag.ENABLED),
    Cannoneer(13, JobFlag.ENABLED),
    Xenon(14, JobFlag.ENABLED),
    Zero(15, JobFlag.ENABLED),
    EunWol(16, JobFlag.ENABLED),
    PinkBean(17, JobFlag.DISABLED),
    Kinesis(18, JobFlag.ENABLED),
    Kadena(19, JobFlag.ENABLED),
    Illium(20, JobFlag.ENABLED),
    Ark(21, JobFlag.ENABLED),
    PathFinder(22, JobFlag.ENABLED),
    Hoyeong(23, JobFlag.ENABLED),
    Adel(24, JobFlag.ENABLED),
    Cain(25, JobFlag.ENABLED),
    Yety(26, JobFlag.DISABLED),
    Lara(27, JobFlag.ENABLED);
    
    private final int jobType;
    
    private final int flag;
    
    LoginJob(int jobType, JobFlag flag) {
      this.jobType = jobType;
      this.flag = flag.getFlag();
    }
    
    public int getJobType() {
      return this.jobType;
    }
    
    public int getFlag() {
      return this.flag;
    }
    
    public enum JobFlag {
      DISABLED(0),
      ENABLED(1),
      LEVEL_DISABLED(2);
      
      private final int flag;
      
      JobFlag(int flag) {
        this.flag = flag;
      }
      
      public int getFlag() {
        return this.flag;
      }
    }
  }
}
