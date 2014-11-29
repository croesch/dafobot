package de.croesch.dafobot.work.buergermeister_slowakei_2014;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * TODO Comment here ...
 *
 * @author dafo
 * @since Date: Nov 29, 2014
 */
public class GemeindeIDEintrager {

  public static void main(final String[] args) throws IOException, SQLException, URISyntaxException {
    final Charset charset = Charset.forName("UTF-8");

    final Properties configuration = new Properties();
    configuration.load(GemeindeIDEintrager.class.getResourceAsStream("/bot.conf"));

    final URL url = GemeindeIDEintrager.class.getResource("/buergermeister_slowakei_2014/list_of_communes.csv");
    final Path path = Paths.get(url.toURI());

    int updated = 0;
    int notUpdated = 0;
    try (final Connection connection = DriverManager.getConnection(configuration.getProperty("db.url.bmsk14"),
                                                                   configuration.getProperty("db.user"),
                                                                   configuration.getProperty("db.password"));
            BufferedReader reader = Files.newBufferedReader(path, charset)) {
      String line = null;
      while ((line = reader.readLine()) != null) {
        final String[] part = line.split(",");
        if (part == null || part.length != 3) {
          throw new IOException("SHIT!");
        }

        try (final PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM `election` WHERE okres=? AND ort=?");) {
          statement.setString(1, part[0]);
          statement.setString(2, part[2]);
          final ResultSet result = statement.executeQuery();
          if (!result.next()) {
            throw new IOException("no result");
          }
          if (result.getInt(1) != 1) {
            System.out.println(line + " not updated");
            ++notUpdated;
            continue;
          }
        }
        try (final PreparedStatement statement = connection.prepareStatement("UPDATE `election` SET id=? WHERE okres=? AND ort=?");) {
          statement.setInt(1, Integer.valueOf(part[1]));
          statement.setString(2, part[0]);
          statement.setString(3, part[2]);
          if (statement.executeUpdate() == 1) {
            ++updated;
          } else {
            System.out.println(line + " not updated!!!!");
            ++notUpdated;
          }
        }
      }
    } finally {
      System.out.println("Updated:\t" + updated);
      System.out.println("Not updated:\t" + notUpdated);
    }
  }
}
