package dataconnectors;

import models.*;

import java.util.HashMap;
import java.util.Map;

public enum EventDataConnector {
    INSTANCE;

    private static ParticipantDataConnector participantDataConnector = ParticipantDataConnector.INSTANCE;

    private Map<String, Event> catalog = new HashMap<>();

    public boolean creatEvent(Event event) {
        catalog.put(event.getId(), event);
        return true;
    }

    public Map<String, Event> getAllEvents() {
        if (catalog.isEmpty()) {
            Organizer sampleOrganizer = OrganizerDataConnector.INSTANCE.getAllOrganizers().entrySet().iterator().next().getValue();
            Address sampleAddress = new Address("Street 1", "Street2", "City", "OH", 43065, "US");
            Map<String, Double> sampleOptions = new HashMap<String, Double>();
            sampleOptions.put("5k", 10.0);

            Event sampleEvent = new Event("Sample Event", sampleAddress, sampleOrganizer.getId(), 10, sampleOptions);
            creatEvent(sampleEvent);
        }

        return catalog;
    }

    public Event getEvent(Event event) {
        return catalog.get(event.getId());
    }

    public Event getEvent(String id) {
        return catalog.get(id);
    }

    public boolean registerParticipant(String eventReferralId, Person participant) {
        return catalog.get(eventReferralId).registerParticipant(participant);
    }
}
