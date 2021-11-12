import java.io.Serial;

/**
 * class for response from server when the registrations is confirmed
 */
public class ClientRegisterConfirmed extends Request  {

    @Serial
    private static final long serialVersionUID = 1L;

    //constructor
    public ClientRegisterConfirmed(int RQNumb){
        super(RequestType.CLIENT_REGISTER_CONFIRMED);
        this.RQNumb = RQNumb;
    }

    /**
     * Get a string of the request
     */
    @Override
    public String toString(){
        return RequestType.CLIENT_REGISTER_CONFIRMED+ " " + this.getRQNumb()+ " " ;
    }
}
