/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.biodbnet.enums;

/**
 * Holds the Taxon name and ID for use with BioDbNet.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public enum Taxon {

    /**
     * No taxon restrictions.
     */
    ALL("All", ""),

    /**
     * African Savanna Elephant.
     */
    AFRICAN_SAVANNA_ELEPHANT("African savanna elephant", "9785"),

    /**
     * Alpaca.
     */
    ALPACA("Alpaca", "30538"),

    /**
     * American Pika.
     */
    AMERICAN_PIKA("American pika", "9978"),

    /**
     * Bottlenosed Dolphin.
     */
    BOTTLENOSED_DOLPHIN("Bottlenosed dolphin", "9738"),

    /**
     * Budding Yeasts.
     */
    BUDDING_YEASTS("Budding yeasts", "4892"),

    /**
     * Cape Rock Hyrax.
     */
    CAPE_ROCK_HYRAX("Cape rock hyrax", "9813"),

    /**
     * Cattle.
     */
    CATTLE("Cattle", "9913"),

    /**
     * Chicken.
     */
    CHICKEN("Chicken", "9031"),

    /**
     * Chimpanzee.
     */
    CHIMPANZEE("Chimpanzee", "9598"),

    /**
     * Dog.
     */
    DOG("Dog", "9615"),

    /**
     * Domestic cat.
     */
    DOMESTIC_CAT("Domestic cat", "9685"),

    /**
     * Domestic Guinea Pig.
     */
    DOMESTIC_GUINEA_PIG("Domestic guinea pig", "10141"),

    /**
     * European Shrew.
     */
    EUROPEAN_SHREW("European shrew", "42254"),

    /**
     * Fruit Fly.
     */
    FRUIT_FLY("Fruit fly", "7227"),

    /**
     * Giant Panda.
     */
    GIANT_PANDA("Giant panda", "9646"),

    /**
     * Gray Mouse Lemur.
     */
    GRAY_MOUSE_LEMUR("Gray mouse lemur", "30608"),

    /**
     * Gray Short-tailed opossum.
     */
    GRAY_SHORT_TAILED_OPOSSUM("Gray short-tailed opossum", "13616"),


    /**
     * Green Anole.
     */
    GREEN_ANOLE("Green anole", "28377"),

    /**
     * Hoffman's Two Fingered Sloth.
     */
    HOFFMANS_TWO_FINGERED_SLOTH("Hoffmann's two-fingered sloth", "9358"),

    /**
     * Horse.
     */
    HORSE("Horse", "9796"),

    /**
     * House Mouse.
     */
    HOUSE_MOUSE("House mouse", "10090"),

    /**
     * Human.
     */
    HUMAN("Human", "9606"),

    /**
     * Japanese Medaka.
     */
    JAPANESE_MEDAKA("Japanese medaka", "8090"),

    /**
     * Large Flying Fox.
     */
    LARGE_FLYING_FOX("Large flying fox", "132908"),

    /**
     * Little Brown Bat.
     */
    LITTLE_BROWN_BAT("Little brown bat", "59463"),

    /**
     * Nine Banded Armadillo.
     */
    NINE_BANDED_ARMADILLO("Nine banded armadillo", "9361"),

    /**
     * Northern Tree Shrew.
     */
    NORTHERN_TREE_SHREW("Northern tree shrew", "37437"),

    /**
     * Northern White-cheeked Gibbon.
     */
    NORTHERN_WHITE_CHEEKED_GIBBON("Northern white-cheeked gibbon", "61853"),

    /**
     * Norway Rat.
     */
    NORWAY_RAT("Norway rat", "10116"),

    /**
     * Ord's Kangaroo Rat.
     */
    ORDS_KANGAROO_RAT("Ord's kangaroo rat", "10020"),

    /**
     * Philippine Tarsier.
     */
    PHILIPPPINE_TARSIER("Philippine tarsier", "9478"),

    /**
     * Pig.
     */
    PIG("Pig", "9832"),


    /**
     * Platypus.
     */
    PLATYPUS("Platypus", "9258"),

    /**
     * Rabbit.
     */
    RABBIT("Rabbit", "9986"),


    /**
     * Rhesus Monkey.
     */
    RHESUS_MONKEY("Rhesus monkey", "9544"),

    /**
     * Roundworm.
     */
    ROUNDWORM("Roundworm", "6231"),

    /**
     * Small Eared Galago.
     */
    SMALL_EARED_GALAGO("Small eared galago", "30611"),

    /**
     * Small Madagascar Hedgehog.
     */
    SMALL_MADAGASCAR_HEDGEHOG("Small Madagascar hedgehog", "9371"),

    /**
     * Spotted Green Pufferfish.
     */
    SPOTTED_GREEN_PUFFERFISH("Spotted green pufferfish", "99883"),

    /**
     * Sumatran Orangutan.
     */
    SUMATRAN_ORANGUTAN("Sumatran orangutan", "9601"),

    /**
     * Thale Cress.
     */
    THALE_CRESS("Thale cross", "3702"),

    /**
     * Thirteen Lined Ground Squirrel.
     */
    THIRTEEN_LINED_GROUND_SQUIRREL("Thirteen lined ground squirrel", "43179"),

    /**
     * Three Spinded Thickleback.
     */
    THREE_SPINED_THICKLEBACK("Three spined stickleback", "69293"),

    /**
     * Torafugu.
     */
    TORAFUGU("Torafugu", "31033"),

    /**
     * Turkey.
     */
    TURKEY("Turkey", "9103"),

    /**
     * Western European Hedgehog.
     */
    WESTERN_EUROPEAN_HEDGEHOG("Western European hedgehog", "9365"),

    /**
     * Western Gorilla.
     */
    WESTERN_GORILLA("Western gorilla", "9593"),

    /**
     * White-tufted-ear Marmoset.
     */
    WHITE_TUFTED_EAR_MARMOSET("White-tufted-ear marmoset", "9483"),

    /**
     * Zebrafish.
     */
    ZEBRAFISH("Zebrafish", "7955");

    private String name;
    private String taxonId;

    private Taxon(String name, String taxonId) {
        this.name = name;
        this.taxonId = taxonId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the value
     */
    public String getTaxonId() {
        return taxonId;
    }
}
