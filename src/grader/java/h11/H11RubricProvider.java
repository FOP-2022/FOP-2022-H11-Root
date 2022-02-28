package h11;

import h11.merge.StreamMergerMetaTests;
import h11.merge.StreamMergerTests;
import h11.supplier.ArraySupplierTests;
import h11.supplier.CollectionSupplierTests;
import h11.supplier.CyclicRangeSupplierTests;
import h11.supplier.SupplierMetaTests;
import h11.utils.transform.BytecodeTransformer;
import h11.unicode.CharFromUnicodeCasesExchangedTests;
import h11.unicode.CharFromUnicodeTests;
import h11.unicode.CharWithIndexTests;
import h11.unicode.FormatExceptionTests;
import h11.unicode.UnicodeMetaTests;
import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.JUnitTestRef;
import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricForSubmission;
import org.sourcegrade.jagr.api.rubric.RubricProvider;
import org.sourcegrade.jagr.api.testing.RubricConfiguration;

import static org.sourcegrade.jagr.api.rubric.Grader.testAwareBuilder;
import static org.sourcegrade.jagr.api.rubric.JUnitTestRef.and;
import static org.sourcegrade.jagr.api.rubric.JUnitTestRef.ofMethod;

/**
 * Rubric Provider for assignment H11.
 */
@RubricForSubmission("h11")
public class H11RubricProvider implements RubricProvider {

    private static final JUnitTestRef H1_1_1_DEFINITION = ofMethod(() ->
        ArraySupplierTests.class.getMethod("testDefinitions"));
    private static final JUnitTestRef H1_1_1_INSTANCE = ofMethod(() ->
        ArraySupplierTests.class.getMethod("testInstance"));
    private static final JUnitTestRef H1_1_2_DEFINITION = ofMethod(() ->
        CollectionSupplierTests.class.getMethod("testDefinitions"));
    private static final JUnitTestRef H1_1_2_INSTANCE = ofMethod(() ->
        CollectionSupplierTests.class.getMethod("testInstance"));
    private static final JUnitTestRef H1_1_2_REQUIREMENT = ofMethod(() ->
        CollectionSupplierTests.class.getMethod("testIllegalInvocations"));
    private static final JUnitTestRef H1_1_3_DEFINITION = ofMethod(() ->
        CyclicRangeSupplierTests.class.getMethod("testDefinitions"));
    private static final JUnitTestRef H1_1_3_INSTANCE = ofMethod(() ->
        CyclicRangeSupplierTests.class.getMethod("testInstance"));

    private static final JUnitTestRef H1_2_DEFINITION = ofMethod(() ->
        SupplierMetaTests.class.getMethod("testDefinitions"));
    private static final JUnitTestRef H1_2_BUILD_INTEGER_ARRAY = ofMethod(() ->
        SupplierMetaTests.class.getMethod("testBuildIntegerArray"));
    private static final JUnitTestRef H1_2_BUILD_INTEGER_LIST = ofMethod(() ->
        SupplierMetaTests.class.getMethod("testBuildIntegerList"));
    private static final JUnitTestRef H1_2_ARRAY_SUPPLIER_TESTS = ofMethod(() ->
        SupplierMetaTests.class.getMethod("metaTest_testArraySupplier"));
    private static final JUnitTestRef H1_2_COLLECTION_SUPPLIER_TESTS = ofMethod(() ->
        SupplierMetaTests.class.getMethod("metaTest_testCollectionSupplier"));
    private static final JUnitTestRef H1_2_CYCLIC_RANGE_SUPPLIER_TESTS = ofMethod(() ->
        SupplierMetaTests.class.getMethod("metaTest_testCyclicRangeSupplier"));

    private static final JUnitTestRef H2_1_DEFINITION = ofMethod(() ->
        FormatExceptionTests.class.getMethod("testDefinitions"));
    private static final JUnitTestRef H2_1_INSTANCE = ofMethod(() ->
        FormatExceptionTests.class.getMethod("testInstance"));

    private static final JUnitTestRef H2_2_1_DEFINITION = ofMethod(() ->
        CharFromUnicodeTests.class.getMethod("testDefinitions"));
    private static final JUnitTestRef H2_2_1_APPLY = ofMethod(() ->
        CharFromUnicodeTests.class.getMethod("testApply"));
    private static final JUnitTestRef H2_2_1_REQUIREMENT = ofMethod(() ->
        CharFromUnicodeTests.class.getMethod("testUnsafeCast"));
    private static final JUnitTestRef H2_2_2_DEFINITION = ofMethod(() ->
        CharFromUnicodeCasesExchangedTests.class.getMethod("testDefinitions"));
    private static final JUnitTestRef H2_2_2_APPLY = ofMethod(() ->
        CharFromUnicodeCasesExchangedTests.class.getMethod("testApply"));
    private static final JUnitTestRef H2_2_2_REQUIREMENT = ofMethod(() ->
        CharFromUnicodeCasesExchangedTests.class.getMethod("testUnsafeCast"));

    private static final JUnitTestRef H2_3_DEFINITION = ofMethod(() ->
        CharWithIndexTests.class.getMethod("testDefinitions"));
    private static final JUnitTestRef H2_3_INSTANCE = ofMethod(() ->
        CharWithIndexTests.class.getMethod("testInstance"));

    private static final JUnitTestRef H2_4_DEFINITION = ofMethod(() ->
        UnicodeMetaTests.class.getMethod("testDefinitions"));
    private static final JUnitTestRef H2_4_META_TESTS = and(
        ofMethod(() -> UnicodeMetaTests.class.getMethod("metaTest_testCharFromUnicode")),
        ofMethod(() -> UnicodeMetaTests.class.getMethod("metaTest_testCharFromUnicodeCasesExchanged"))
    );

    private static final JUnitTestRef H3_1_DEFINITION = ofMethod(() ->
        StreamMergerTests.class.getMethod("testDefinitions"));
    private static final JUnitTestRef H3_1_PREDICATE = ofMethod(() ->
        StreamMergerTests.class.getMethod("testPredicate"));
    private static final JUnitTestRef H3_1_COMPARATOR = ofMethod(() ->
        StreamMergerTests.class.getMethod("testComparator"));
    private static final JUnitTestRef H3_1_FUNCTION = ofMethod(() ->
        StreamMergerTests.class.getMethod("testFunction"));
    private static final JUnitTestRef H3_1_COLLECTOR_SUPPLIER_FINISHER = ofMethod(() ->
        StreamMergerTests.class.getMethod("testCollectorSupplierFinisher"));
    private static final JUnitTestRef H3_1_COLLECTOR_ACCUMULATOR = ofMethod(() ->
        StreamMergerTests.class.getMethod("testAccumulator"));
    private static final JUnitTestRef H3_1_COLLECTOR_COMBINER = ofMethod(() ->
        StreamMergerTests.class.getMethod("testCombiner"));
    private static final JUnitTestRef H3_1_MERGE = ofMethod(() ->
        StreamMergerTests.class.getMethod("testMerge"));

    private static final JUnitTestRef H3_2_DEFINITION = ofMethod(() ->
        StreamMergerMetaTests.class.getMethod("testDefinitions"));
    private static final JUnitTestRef H3_2_TEST_MERGE_INDEX_0 = ofMethod(() ->
        StreamMergerMetaTests.class.getMethod("testIndex0Correct"));
    private static final JUnitTestRef H3_2_TEST_MERGE_INDEX_1 = ofMethod(() ->
        StreamMergerMetaTests.class.getMethod("testIndex1Correct"));
    private static final JUnitTestRef H3_2_TEST_MERGE_INDEX_2 = ofMethod(() ->
        StreamMergerMetaTests.class.getMethod("testIndex2Correct"));

    private static final Criterion H1_1_1_1 = makeCriterionFor(H1_1_1_DEFINITION,
        "Klasse h11.supplier.ArraySupplier und ihre Methoden sind korrekt definiert.");
    private static final Criterion H1_1_1_2 = makeCriterionFor(H1_1_1_INSTANCE,
        "Es kann ein Objekt von h11.supplier.ArraySupplier erzeugt werden und Methode "
            + "apply liefert korrekte Ergebnisse.");
    private static final Criterion H1_1_2_1 = makeCriterionFor(H1_1_2_DEFINITION,
        "Klasse h11.supplier.CollectionSupplier und ihre Methoden sind korrekt definiert.");
    private static final Criterion H1_1_2_2 = makeCriterionFor(H1_1_2_INSTANCE,
        "Es kann ein Objekt von h11.supplier.CollectionSupplier erzeugt werden und Methode "
            + "apply liefert korrekte Ergebnisse.");
    private static final Criterion H1_1_2_R = Criterion
        .builder()
        .shortDescription("Es wurden keine verbotenen Methoden verwendet.")
        .minPoints(-1)
        .maxPoints(0)
        .grader(
            testAwareBuilder()
                .requirePass(H1_1_2_REQUIREMENT)
                .pointsFailedMin()
                .pointsPassedMax()
                .build()
        )
        .build();
    private static final Criterion H1_1_3_1 = makeCriterionFor(H1_1_3_DEFINITION,
        "Klasse h11.supplier.CyclicRangeSupplier und ihre Methoden sind korrekt definiert.");
    private static final Criterion H1_1_3_2 = makeCriterionFor(H1_1_3_INSTANCE,
        "Es kann ein Objekt von h11.supplier.CyclicRangeSupplier erzeugt werden und Methode "
            + "apply liefert korrekte Ergebnisse.");

    private static final Criterion H1_2_1 = makeCriterionFor(H1_2_DEFINITION,
        "Klasse h11.supplier.SupplierTests und ihre Methoden sind korrekt definiert.");
    private static final Criterion H1_2_2 = makeCriterionFor(H1_2_BUILD_INTEGER_ARRAY,
        "Methode buildIntegerArray liefert korrekte Ergebnisse.");
    private static final Criterion H1_2_3 = makeCriterionFor(H1_2_BUILD_INTEGER_LIST,
        "Methode buildIntegerList liefert korrekte Ergebnisse.");
    private static final Criterion H1_2_4 = makeCriterionFor(H1_2_ARRAY_SUPPLIER_TESTS,
        "Methode testArraySupplier ist wie angegeben implementiert.");
    private static final Criterion H1_2_5 = makeCriterionFor(H1_2_COLLECTION_SUPPLIER_TESTS,
        "Methode testCollectionSupplier ist wie angegeben implementiert.");
    private static final Criterion H1_2_6 = makeCriterionFor(H1_2_CYCLIC_RANGE_SUPPLIER_TESTS,
        "Methode testCyclicRangeSupplier ist wie angegeben implementiert.");

    private static final Criterion H2_1_1 = makeCriterionFor(H2_1_DEFINITION,
        "Klasse h11.unicode.FormatException ist korrekt definiert.");
    private static final Criterion H2_1_2 = makeCriterionFor(H2_1_INSTANCE,
        "Es kann ein Objekt von h11.unicode.FormatException erzeugt werden "
            + "und die Nachricht der Exception ist korrekt.");

    private static final Criterion H2_2_1 = makeCriterionFor(H2_2_1_DEFINITION,
        "Klasse h11.unicode.CharFromUnicode und ihre Methode sind korrekt definiert.");
    private static final Criterion H2_2_2 = makeCriterionFor(H2_2_1_APPLY,
        "Methode apply von h11.unicode.CharFromUnicode liefert korrekte Ergebnisse.");
    private static final Criterion H2_2_R1 = Criterion
        .builder()
        .shortDescription("Es wurden keine unsicheren Konversionen nach char vorgenommen.")
        .minPoints(-1)
        .maxPoints(0)
        .grader(
            testAwareBuilder()
                .requirePass(H2_2_1_REQUIREMENT)
                .pointsFailedMin()
                .pointsPassedMax()
                .build()
        )
        .build();
    private static final Criterion H2_2_3 = makeCriterionFor(H2_2_2_DEFINITION,
        "Klasse h11.unicode.CharFromUnicodeCasesExchanged und ihre Methode sind korrekt definiert.");
    private static final Criterion H2_2_4 = makeCriterionFor(H2_2_2_APPLY,
        "Methode apply von h11.unicode.CharFromUnicodeCasesExchanged liefert korrekte Ergebnisse.");
    private static final Criterion H2_2_R2 = Criterion
        .builder()
        .shortDescription("Es wurden keine unsicheren Konversionen nach char vorgenommen.")
        .minPoints(-1)
        .maxPoints(0)
        .grader(
            testAwareBuilder()
                .requirePass(H2_2_2_REQUIREMENT)
                .pointsFailedMin()
                .pointsPassedMax()
                .build()
        )
        .build();

    private static final Criterion H2_3_1 = makeCriterionFor(H2_3_DEFINITION,
        "Klasse h11.unicode.CharWithIndex ist korrekt definiert.");
    private static final Criterion H2_3_2 = makeCriterionFor(H2_3_INSTANCE,
        "Es kann ein Objekt von h11.unicode.CharWithIndex erzeugt werden, "
            + "alle Attribute werden korrekt initialisiert und "
            + "die beiden Getter-Methoden liefern korrekte Ergebnisse zurück.");

    private static final Criterion H2_4_1 = makeCriterionFor(H2_4_DEFINITION,
        "Klasse h11.unicode.UnicodeTests und ihre Methoden sind korrekt definiert.");
    private static final Criterion H2_4_2 = makeCriterionFor(H2_4_META_TESTS,
        "Die Tests für h11.unicode.CharFromUnicode und h11.unicode.CharFromUnicodeCasesExchanged "
            + "sind wie gefordert implementiert.");

    private static final Criterion H3_1_1 = makeCriterionFor(H3_1_DEFINITION,
        "Klasse h11.merge.StreamMerger und ihre Attribute sowie "
            + "die Methode merge sind korrekt definiert.");
    private static final Criterion H3_1_2 = makeCriterionFor(H3_1_PREDICATE,
        "Das Prädikat im Attribut predicate funktioniert wie angegeben.");
    private static final Criterion H3_1_3 = makeCriterionFor(H3_1_COMPARATOR,
        "Der Comparator im Attribut comparator funktioniert wie angegeben.");
    private static final Criterion H3_1_4 = makeCriterionFor(H3_1_FUNCTION,
        "Die Funktion im Attribut function funktioniert wie angegeben.");
    private static final Criterion H3_1_5 = makeCriterionFor(H3_1_COLLECTOR_SUPPLIER_FINISHER,
        "Container-Supplier und Finisher-Funktion von collector funktionieren wie angegeben.");
    private static final Criterion H3_1_6 = makeCriterionFor(H3_1_COLLECTOR_ACCUMULATOR,
        "Der Akkumulator von collector funktioniert wie angegeben.");
    private static final Criterion H3_1_7 = makeCriterionFor(H3_1_COLLECTOR_COMBINER,
        "Die Funktion von collector zum Kombinieren zweier Container funktioniert wie angegeben.");
    private static final Criterion H3_1_8 = makeCriterionFor(H3_1_MERGE,
        "Methode merge funktioniert wie angegeben.");

    private static final Criterion H3_2_1 = makeCriterionFor(H3_2_DEFINITION,
        "Klasse h11.merge.StreamMergerTest und Methode testMerge sind korrekt definiert.");
    private static final Criterion H3_2_2 = makeCriterionFor(H3_2_TEST_MERGE_INDEX_0,
        "An Index 0 des zum Testen von merge verwendeten Arrays wurde mit Stream.of(...) gearbeitet.");
    private static final Criterion H3_2_3 = makeCriterionFor(H3_2_TEST_MERGE_INDEX_1,
        "An Index 1 des zum Testen von merge verwendeten Arrays wurde mit Stream.generate(...) gearbeitet.");
    private static final Criterion H3_2_4 = makeCriterionFor(H3_2_TEST_MERGE_INDEX_2,
        "An Index 2 des zum Testen von merge verwendeten Arrays wurde mit IntStream.range(...) gearbeitet.");

    private static final Criterion H1_1 = makeCriterionFor("H1.1 | Supplier-Implementationen",
            H1_1_1_1, H1_1_1_2, H1_1_2_1, H1_1_2_2, H1_1_2_R, H1_1_3_1, H1_1_3_2);

    private static final Criterion H1_2 = makeCriterionFor("H1.2 | Tests für die Supplier",
            H1_2_1, H1_2_2, H1_2_3, H1_2_4, H1_2_5, H1_2_6);

    private static final Criterion H2_1 = makeCriterionFor("H2.1 | Exceptionklasse für ungültige code points",
            H2_1_1, H2_1_2);

    private static final Criterion H2_2 = makeCriterionFor("H2.2 | Umwandlung von code point zu Zeichen",
            H2_2_1, H2_2_2, H2_2_R1, H2_2_3, H2_2_4, H2_2_R2);

    private static final Criterion H2_3 = makeCriterionFor("H2.3 | Paar aus Zeichen und Zahl",
            H2_3_1, H2_3_2);

    private static final Criterion H2_4 = makeCriterionFor("H2.4 | Tests für Package h11.unicode",
            H2_4_1, H2_4_2);

    private static final Criterion H3_1 = makeCriterionFor("H3.1 | Arbeiten mit Streams",
            H3_1_1, H3_1_2, H3_1_3, H3_1_4, H3_1_5, H3_1_6, H3_1_7, H3_1_8);

    private static final Criterion H3_2 = makeCriterionFor("H3.2 | Tests für StreamMerger",
            H3_2_1, H3_2_2, H3_2_3, H3_2_4);

    @Override
    public Rubric getRubric() {
        return Rubric
            .builder()
            .title("H11 | Streams")
            .addChildCriteria(H1_1, H1_2, H2_1, H2_2, H2_3, H2_4, H3_1, H3_2)
            .build();
    }

    @Override
    public void configure(RubricConfiguration configuration) {
        configuration.addTransformer(new BytecodeTransformer());
    }

    private static Criterion makeCriterionFor(JUnitTestRef testRef, String shortDesc) {
        return Criterion
            .builder()
            .shortDescription(shortDesc)
            .grader(
                testAwareBuilder()
                    .requirePass(testRef)
                    .pointsFailedMin()
                    .pointsPassedMax()
                    .build()
            )
            .build();
    }

    private static Criterion makeCriterionFor(String shortDesc, Criterion... criteria) {
        return Criterion
            .builder()
            .shortDescription(shortDesc)
            .addChildCriteria(criteria)
            .build();
    }
}
