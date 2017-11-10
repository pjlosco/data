package controllers;

import dataconnectors.ParticipantDataConnector;
import models.Address;
import models.DateOfBirth;
import models.Person;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

import static play.libs.Scala.asScala;

public class ParticipantController extends Controller {

    private final Form<ParticipantData> form;
    private static ParticipantDataConnector participantDataConnector = ParticipantDataConnector.INSTANCE;

    @Inject
    public ParticipantController(FormFactory formFactory) {
        this.form = formFactory.form(ParticipantData.class);
        Address sampleAddress = new Address("Street 1", "Street2", "City", "OH", 43065, "US");
        DateOfBirth dateOfBirth = new DateOfBirth(1, 1, 1975);
        Person person = new Person("Joe", "Cool", dateOfBirth, "email", "555-654-3211", "male", sampleAddress);
        participantDataConnector.registerParticipant(person);
    }

    public Result viewAllParticipants() {
        return ok(views.html.listParticipants.render(asScala(participantDataConnector.getAllParticipants().values()), form));
    }

    public Result createParticipant() {
        final Form<ParticipantData> boundForm = form.bindFromRequest();
        // TODO - check for existing email

        if (boundForm.hasErrors()) {
            play.Logger.ALogger logger = play.Logger.of(getClass());
            logger.error("errors = {}", boundForm.errors());
            return badRequest(views.html.listParticipants.render(asScala(participantDataConnector.getAllParticipants().values()), boundForm));
        } else {
            ParticipantData data = boundForm.get();
            Address address = new Address(data.getStreet1(), data.getStreet2(), data.getCity(), data.getState(), data.getZip(), data.getCountry());
            DateOfBirth dateOfBirth = new DateOfBirth(data.getDay(), data.getMonth(), data.getYear());
            Person participant = new Person(data.getFirstName(), data.getLastName(), dateOfBirth, data.getEmail(), data.getMobileNumber(), data.getGender(), address);
            participantDataConnector.registerParticipant(participant);
            flash("info", "Participant added!");
            return redirect(routes.ParticipantController.viewAllParticipants());
        }
    }
}
