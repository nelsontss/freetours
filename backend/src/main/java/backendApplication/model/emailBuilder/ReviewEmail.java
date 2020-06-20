package backendApplication.model.emailBuilder;

/**
 * Class represents a Concrete Builder, in this case specifies a Review Email.
 * */
public class ReviewEmail extends EmailBuilder{

    @Override
    protected void setSubject(String subject) {

        email.setSubject("TODO");

    }

    @Override
    protected void setText(String text) {

        email.setText("TODO");

    }

}