package Client_Achat.protocol;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Libsocket{
    private final OutputStream dos;
    private final InputStream dis;

    public Libsocket(String target_ip, int target_port) throws IOException {
        Socket client_socket = new Socket(target_ip, target_port);
        dos = client_socket.getOutputStream();
        dis = client_socket.getInputStream();
    }
    public int SendMsg(String data) throws IOException {
        ByteBuffer packet = ByteBuffer.allocate(1024);
        packet.mark();
        List<String> data_body_packets= new ArrayList<>();

        packet.order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < data.length(); i += 1019)
            data_body_packets.add(data.substring(i, Math.min(data.length(), i + 1019)));

        for (String dataBodyPacket : data_body_packets) {
            if (dataBodyPacket.length() >= 1019) {
                packet.put((byte) 1);
                packet.putInt(1019);
                packet.put(dataBodyPacket.getBytes());
            } else {
                packet.put((byte) 0);
                packet.putInt(dataBodyPacket.length());
                packet.put(dataBodyPacket.getBytes());
            }
            dos.write(packet.array(), 0, dataBodyPacket.length() + 5);
            packet.reset();
        }
        return 0;

    }

    public String Receive_msg() throws IOException {
        byte has_next_packet = 0;
        byte[] header;
        StringBuilder message = new StringBuilder();

        do {
            header = dis.readNBytes(5);
            has_next_packet = header[0];
            String current_message = new String(dis.readNBytes(header[1]));
            message.append(current_message);

        }while (has_next_packet == 1);

        return message.toString();
    }
}
