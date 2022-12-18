package io.zetch.app.seed;

import io.zetch.app.domain.location.LocationEntity;
import io.zetch.app.domain.location.Type;
import java.util.List;

/** Sample locations. */
public class SeedLocations {

  public static final LocationEntity LOCATION1 =
      LocationEntity.builder()
          .name("Whitney Museum of American Art")
          .description(
              "The Whitney Museum of American Art is an art museum in the "
                  + "Meatpacking District and West Village neighborhoods of"
                  + " Manhattan in New York City.")
          .address("1100 Riverside Drive")
          .type(Type.MUSEUM)
          .owners(List.of(SeedUsers.USER2))
          .build();
  public static final LocationEntity LOCATION2 =
      LocationEntity.builder()
          .name("The Frick Collection")
          .description(
              " Its permanent collection features Old Master paintings "
                  + "and European fine and decorative arts, including "
                  + "works by Bellini, Fragonard, Goya, Holbein, "
                  + "Rembrandt, Titian, Turner, Velazquez, Vermeer,"
                  + " Thomas Gainsborough, and many others.")
          .address("3920 Broadway")
          .type(Type.RESTAURANT)
          .owners(List.of(SeedUsers.USER3))
          .build();
  public static final LocationEntity LOCATION3 =
      LocationEntity.builder()
          .name("Tenement Museum")
          .description(
              "Explore historically recreated tenement apartments "
                  + "and discover how immigrants lived on New York's Lower East "
                  + "Side in the 19th and 20th centuries.")
          .address("4029 Morningside Drive")
          .type(Type.MUSEUM)
          .owners(List.of(SeedUsers.USER4))
          .build();
  public static final LocationEntity LOCATION4 =
      LocationEntity.builder()
          .name("Brooklyn Museum")
          .description(
              "The Brooklyn Museum is an art museum located in the "
                  + "New York City borough of Brooklyn. At 560,000 square feet,"
                  + " the museum is New York City's second largest and contains "
                  + "an art collection with around 1.5 million objects.")
          .address("1323 St. Nicholas Ave")
          .owners(List.of(SeedUsers.USER2))
          .type(Type.MUSEUM)
          .build();
  public static final LocationEntity LOCATION5 =
      LocationEntity.builder()
          .name("Eugene Museum of Music")
          .description(
              "A museum dedicated to preservation and celebration of "
                  + "the history of all good music, this idea for the "
                  + "museum was conceived in 1995 by generous philanthropist Eugene Kim.")
          .address("263th E 103 St")
          .type(Type.MUSEUM)
          .owners(List.of(SeedUsers.USER2))
          .build();
  public static final LocationEntity LOCATION6 =
      LocationEntity.builder()
          .name("Columbia Museum")
          .description(
              "Located on the campus of Columbia University, this museum plays "
                  + "a major role in developing and collecting modern art, and is "
                  + "often identified as one of the largest and most influential "
                  + "museums of modern art in the world. ")
          .address("569th W 120 St")
          .type(Type.MUSEUM)
          .owners(List.of(SeedUsers.USER2))
          .build();
  public static final LocationEntity LOCATION7 =
      LocationEntity.builder()
          .name("Harlem Portrait Museum")
          .description(
              "An art museum in Harlem, New York City. Its permanent collection "
                  + "features Old Master paintings and European fine and"
                  + " decorative arts, including works by Bellini, Fragonard, "
                  + "Vermeer, Thomas Gainsborough, and many others. ")
          .address("1 E 70th St, New York, NY 10021")
          .type(Type.MUSEUM)
          .owners(List.of(SeedUsers.USER3))
          .build();
  public static final LocationEntity LOCATION8 =
      LocationEntity.builder()
          .name("Museum of Sex")
          .description(
              "For over fifteen years, the Museum of Sex "
                  + "has sustained its mission to preserve and present the history, "
                  + "evolution, and cultural significance of human sexuality ")
          .address("233 5th Ave, New York, NY 10017")
          .type(Type.MUSEUM)
          .owners(List.of(SeedUsers.USER4))
          .build();
  public static final List<LocationEntity> LOCATIONS =
      List.of(
          LOCATION1, LOCATION2, LOCATION3, LOCATION4, LOCATION5, LOCATION6, LOCATION7, LOCATION8);

  private SeedLocations() {
    throw new IllegalStateException("Utility class");
  }
}
