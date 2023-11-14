package at.fhv.master.laendleenergy.datacollector;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import io.quarkus.runtime.StartupEvent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.TimeZone;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class Test {


    public void onStart(@Observes StartupEvent event) {
        System.out.println("Test bean has been initialized.");
    }

    @Incoming("simulator")
    public CompletionStage<Void> consume(Message<byte[]> message) {
        // process your price.
        System.out.println(parseData(message.getPayload()));
        // Acknowledge the incoming message
        return message.ack();
    }


    public String parseData(byte[] message) {

        System.out.println(message.length);

        if (message.length == 44) {
            long timestampRaw = message[0] << 24 | message[1] << 16 | message[2] << 8 | message[3];
            LocalDateTime timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampRaw),
                    TimeZone.getTimeZone("UTC").toZoneId());
            float instantaneousActivePowerPlusW = ByteBuffer.wrap(Arrays.copyOfRange(message, 0, 4)).order(ByteOrder.LITTLE_ENDIAN).getFloat();
            //float instantaneousActivePowerPlusW = message[4] << 24 | message[5] << 16 | message[6] << 8 | message[7];
            float totalEnergyConsumedWh = message[8] << 24 | message[9] << 16 | message[10] << 8 | message[11];
            float instantaneousActivePowerMinusW = message[12] << 24 | message[13] << 16 | message[14] << 8 | message[15];
            float totalEnergyDeliveredWh = message[16] << 24 | message[17] << 16 | message[18] << 8 | message[19];
            float currentL1A = message[20] << 24 | message[21] << 16 | message[22] << 8 | message[23];
            float voltageL1V = message[24] << 24 | message[25] << 16 | message[26] << 8 | message[27];
            float currentL2A = message[28] << 24 | message[29] << 16 | message[30] << 8 | message[31];
            float voltageL2V = message[32] << 24 | message[33] << 16 | message[34] << 8 | message[35];
            float currentL3A = message[36] << 24 | message[37] << 16 | message[38] << 8 | message[39];
            float voltageL3V = message[40] << 24 | message[41] << 16 | message[42] << 8 | message[43];

            MQTTMessage mqttMessage = new MQTTMessage(timestamp, currentL1A, currentL2A, currentL3A,
                    voltageL1V, voltageL2V, voltageL3V, instantaneousActivePowerPlusW, instantaneousActivePowerMinusW,
                    totalEnergyConsumedWh, totalEnergyDeliveredWh);

            return mqttMessage.toString();
        }
        return "Message length != 44";
    }
}
