package fr.esgi.rent.beans;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.stream.Stream;

import static fr.esgi.rent.beans.EnergyClassification.D;
import static fr.esgi.rent.beans.PropertyType.FLAT;
import static fr.esgi.rent.samples.RentalPropertySample.oneRentalProperty;
import static org.assertj.core.api.Assertions.assertThat;

class RentalPropertyTest {

    private static final String[] HEADERS = {
            "Id",
            "Description",
            "Ville",
            "Adresse",
            "Type de propriété",
            "Loyer mensuel",
            "Dépôt de garantie",
            "Surface",
            "Nombre de chambres",
            "Numéro d'étage",
            "Nombre d'étages",
            "Année de construction",
            "Classification énergétique",
            "Ascenseur",
            "Interphone",
            "Balcon",
            "Place de parking"
    };

    @ParameterizedTest
    @MethodSource("provideCsvValuesAndExpectedRentalProperty")
    void shouldCreate(String[] csvValues, RentalProperty expectedRentalProperty) throws IOException {
        String csvLine = String.join(",", csvValues);
        CSVFormat csvFormat = CSVFormat.Builder.create().setHeader(HEADERS).build();
        CSVParser parser = csvFormat.parse(new StringReader(csvLine));
        CSVRecord csvRecord = parser.getRecords().get(0);

        RentalProperty rentalProperty = RentalProperty.create(csvRecord, HEADERS, csvField -> true);

        assertThat(rentalProperty).isEqualTo(expectedRentalProperty);
    }

    private static Stream<Arguments> provideCsvValuesAndExpectedRentalProperty() {
        String[] csvValues = {"46890", "Appartement spacieux avec vue sur l'ESGI", "Paris", "77 Rue des roses", "Appartement", "750.90", "1200.90", "37.48", "2", "1", "3", "1990", "D", "non", "non", "oui", "non"};
        String[] csvValuesWithUnknownPropertyType = {"46890", "Appartement spacieux avec vue sur l'ESGI", "Paris", "77 Rue des roses", "Unknown", "750.90", "1200.90", "37.48", "2", "1", "3", "1990", "D", "non", "non", "oui", "non"};
        String[] csvValuesWithUnknownEnergyClassification = {"46890", "Appartement spacieux avec vue sur l'ESGI", "Paris", "77 Rue des roses", "Appartement", "750.90", "1200.90", "37.48", "2", "1", "3", "1990", "Unknown", "non", "non", "oui", "non"};

        RentalProperty rentalPropertyWithNullPropertyType = new RentalProperty(
                46890,
                "Appartement spacieux avec vue sur l'ESGI",
                "Paris",
                "77 Rue des roses",
                null,
                750.90,
                1200.90,
                37.48,
                2,
                0,
                0,
                1990,
                D,
                true,
                true,
                true,
                true);

        RentalProperty rentalPropertyWithNullEnergyClassification = new RentalProperty(
                46890,
                "Appartement spacieux avec vue sur l'ESGI",
                "Paris",
                "77 Rue des roses",
                FLAT,
                750.90,
                1200.90,
                37.48,
                2,
                1,
                3,
                1990,
                null,
                true,
                true,
                true,
                true);

        return Stream.of(
                Arguments.of(csvValues, oneRentalProperty()),
                Arguments.of(csvValuesWithUnknownPropertyType, rentalPropertyWithNullPropertyType),
                Arguments.of(csvValuesWithUnknownEnergyClassification, rentalPropertyWithNullEnergyClassification));
    }

}
