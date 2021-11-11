import java.io.Serial;

/**
 * class for response from server when the registrations is denied
 */
public class ClientRegisterDenied extends Request  {

    @Serial
    private static final long serialVersionUID = 1L;
    String reason;

    /**
     * constructor of a denied register
     * @param reason
     * @param rid
     */
    public ClientRegisterDenied(String reason, int rid){
      super(RequestType.CLIENT_REGISTER_DENIED);
      this.RQNumb = rid;
      this.reason=reason;
    }

    //getter
    public String getReason() {
        return reason;
    }

    //string of the response
    @Override
    public String toString(){
        return RequestType.CLIENT_REGISTER_DENIED+ " "  + this.getRQNumb()+ " '" + getReason() + "'";
    }
}

