package keycontacts.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import keycontacts.commons.exceptions.IllegalValueException;
import keycontacts.model.lesson.MakeupLesson;
import keycontacts.model.lesson.RegularLesson;
import keycontacts.model.pianopiece.PianoPiece;
import keycontacts.model.student.Address;
import keycontacts.model.student.GradeLevel;
import keycontacts.model.student.Name;
import keycontacts.model.student.Phone;
import keycontacts.model.student.Student;

/**
 * Jackson-friendly version of {@link Student}.
 */
class JsonAdaptedStudent {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Student's %s field is missing!";

    private final String name;
    private final String phone;
    private final String address;
    private final String gradeLevel;
    private final List<JsonAdaptedPianoPiece> pianoPieces = new ArrayList<>();
    private final List<JsonAdaptedMakeupLesson> makeupLessons = new ArrayList<>();
    private final JsonAdaptedRegularLesson regularLesson;

    /**
     * Constructs a {@code JsonAdaptedStudent} with the given student details.
     */
    @JsonCreator
    public JsonAdaptedStudent(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
                              @JsonProperty("address") String address, @JsonProperty("gradeLevel") String gradeLevel,
                              @JsonProperty("pianoPieces") List<JsonAdaptedPianoPiece> pianoPieces,
                              @JsonProperty("regularLesson") JsonAdaptedRegularLesson regularLesson,
                              @JsonProperty("makeupLessons") List<JsonAdaptedMakeupLesson> makeupLessons) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.gradeLevel = gradeLevel;
        if (pianoPieces != null) {
            this.pianoPieces.addAll(pianoPieces);
        }
        this.regularLesson = regularLesson;
        if (makeupLessons != null) {
            this.makeupLessons.addAll(makeupLessons);
        }
    }

    /**
     * Converts a given {@code Student} into this class for Jackson use.
     */
    public JsonAdaptedStudent(Student source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        address = source.getAddress().value;
        gradeLevel = source.getGradeLevel().value;
        pianoPieces.addAll(source.getPianoPieces().stream()
                .map(JsonAdaptedPianoPiece::new)
                .collect(Collectors.toList()));
        regularLesson = source.getRegularLesson().map(JsonAdaptedRegularLesson::new).orElse(null);
        makeupLessons.addAll(source.getMakeupLessons().stream()
                .map(JsonAdaptedMakeupLesson::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted student object into the model's {@code Student} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted student.
     */
    public Student toModelType() throws IllegalValueException {
        final List<PianoPiece> studentPianoPieces = new ArrayList<>();
        for (JsonAdaptedPianoPiece pianoPiece : pianoPieces) {
            studentPianoPieces.add(pianoPiece.toModelType());
        }

        final List<MakeupLesson> studentMakeupLessons = new ArrayList<>();
        for (JsonAdaptedMakeupLesson makeupLesson : makeupLessons) {
            studentMakeupLessons.add(makeupLesson.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        if (gradeLevel == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    GradeLevel.class.getSimpleName()));
        }
        if (!GradeLevel.isValidGradeLevel(gradeLevel)) {
            throw new IllegalValueException(GradeLevel.MESSAGE_CONSTRAINTS);
        }
        final GradeLevel modelGradeLevel = new GradeLevel(gradeLevel);

        final Set<PianoPiece> modelPianoPieces = new HashSet<>(studentPianoPieces);

        final RegularLesson modelRegularLesson;
        if (regularLesson != null) {
            modelRegularLesson = regularLesson.toModelType();
        } else {
            modelRegularLesson = null;
        }
        final Set<MakeupLesson> modelMakeupLessons = new HashSet<>(studentMakeupLessons);

        return new Student(modelName, modelPhone, modelAddress, modelGradeLevel, modelPianoPieces,
            modelRegularLesson, modelMakeupLessons);
    }

}
