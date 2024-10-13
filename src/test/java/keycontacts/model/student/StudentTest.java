package keycontacts.model.student;

import static keycontacts.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static keycontacts.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static keycontacts.logic.commands.CommandTestUtil.VALID_GRADE_LEVEL_BOB;
import static keycontacts.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static keycontacts.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static keycontacts.logic.commands.CommandTestUtil.VALID_PIANO_PIECE_BEETHOVEN;
import static keycontacts.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static keycontacts.testutil.Assert.assertThrows;
import static keycontacts.testutil.TypicalStudents.ALICE;
import static keycontacts.testutil.TypicalStudents.BOB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import keycontacts.model.pianopiece.PianoPiece;
import keycontacts.model.util.SampleDataUtil;
import keycontacts.testutil.StudentBuilder;

public class StudentTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Student student = new StudentBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> student.getTags().remove(0));
    }

    @Test
    public void isSameStudent() {
        // same object -> returns true
        assertTrue(ALICE.isSameStudent(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameStudent(null));

        // same name, all other attributes different -> returns true
        Student editedAlice = new StudentBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSameStudent(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new StudentBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSameStudent(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Student editedBob = new StudentBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.isSameStudent(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new StudentBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSameStudent(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Student aliceCopy = new StudentBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different student -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Student editedAlice = new StudentBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new StudentBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new StudentBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new StudentBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new StudentBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));

        // different grade level -> returns false
        editedAlice = new StudentBuilder(ALICE).withGradeLevel(VALID_GRADE_LEVEL_BOB).build();
      
        // different piano pieces -> returns false
        editedAlice = new StudentBuilder(ALICE).withPianoPieces(VALID_PIANO_PIECE_BEETHOVEN).build();
      
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Student.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail() + ", address=" + ALICE.getAddress() + ", tags=" + ALICE.getTags()
                + ", gradeLevel=" + ALICE.getGradeLevel() + ", pianoPieces=" + ALICE.getPianoPieces() 
                + ", regularLesson=" + ALICE.getRegularLessonString() + "}";
        assertEquals(expected, ALICE.toString());
    }

    @Test
    public void withAddedPianoPieces() {
        Student student = new StudentBuilder(ALICE).withPianoPieces().build();

        Set<PianoPiece> pianoPieces = SampleDataUtil
                .getPianoPieceSet("Für Elise", "Moonlight Sonata", "Franz Liszt – Liebestraum No. 3");
        Student updatedStudent = student.withAddedPianoPieces(pianoPieces);
        assertEquals(pianoPieces, updatedStudent.getPianoPieces());
    }
}
