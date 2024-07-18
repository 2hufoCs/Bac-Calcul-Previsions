import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BacCalcul {

    // La liste qui contient toutes les matières et épreuves
    public Matiere matieres[] = {
            new Matiere("Histoire/Geo", 6, Annees.PREMIERE_ET_TERMINALE),
            new Matiere("LVA", 6, Annees.PREMIERE_ET_TERMINALE),
            new Matiere("LVB", 6, Annees.PREMIERE_ET_TERMINALE),
            new Matiere("Enseignement Scientifique", 6, Annees.PREMIERE_ET_TERMINALE),
            new Matiere("EPS", 6, Annees.TERMINALE),
            new Matiere("Specialite abandonnee en terminale", 8, Annees.PREMIERE),
            new Matiere("EMC", 2, Annees.PREMIERE_ET_TERMINALE),
            new Matiere("Epreuve de specialite 1", 16, Annees.TERMINALE),
            new Matiere("Epreuve de specialite 2", 16, Annees.TERMINALE),
            new Matiere("Oral de francais", 5, Annees.PREMIERE),
            new Matiere("Ecrit de francais", 5, Annees.PREMIERE),
            new Matiere("Philosophie", 8, Annees.TERMINALE),
            new Matiere("Grand oral", 10, Annees.TERMINALE)
    };

    JFrame f = new JFrame();
    ArrayList<JLabel> tempLabels = new ArrayList<JLabel>();

    public static void main(String args[]) {
        BacCalcul bacCalcul = new BacCalcul();
        bacCalcul.showInterface();
    }

    private void showInterface() {
        f.setTitle("Bac Calcul");
        f.setBounds(900, 200, 800, 600);

        // Ce hashmap contient tous les champs pour entrer les notes
        HashMap<Matiere, ArrayList<JTextField>> noteFields = new HashMap<Matiere, ArrayList<JTextField>>();
        for (int i = 0; i < matieres.length; i++) {
            int gap = i >= 7 ? 30 * (i + 5) : 30 * (i + 3);
            JLabel matiereLabel = new JLabel(matieres[i].getNom());
            matiereLabel.setBounds(50, gap, 250, 25);
            f.add(matiereLabel);

            ArrayList<JTextField> notes = new ArrayList<JTextField>();

            // Si les notes de premiere sont concernées
            if (matieres[i].getPremiere()) {
                JTextField note1 = new JTextField();
                note1.setBounds(matiereLabel.getX() + matiereLabel.getWidth() + 20, matiereLabel.getY(), 50, 20);
                notes.add(note1);
                f.add(note1);
            }

            // Si les notes de terminale sont concernées
            if (matieres[i].getTerminale()) {
                JTextField note2 = new JTextField();
                note2.setBounds(matiereLabel.getX() + matiereLabel.getWidth() + 100, matiereLabel.getY(), 50, 20);
                notes.add(note2);
                f.add(note2);
            }
            noteFields.put(matieres[i], notes);
        }

        // 1ere partie du formulaire
        JLabel title1 = new JLabel("Contrôle continu");
        title1.setBounds(100, 0, 300, 50);
        title1.setFont(new Font("Dialog", 1, 20));
        f.add(title1);

        // 2eme partie du formulaire
        JLabel title2 = new JLabel("Epreuves");
        title2.setBounds(100, 300, 300, 50);
        title2.setFont(new Font("Dialog", 1, 20));
        f.add(title2);

        // 1ere colonne
        JLabel premiereLabel = new JLabel("Première");
        premiereLabel.setBounds(320, 50, 80, 20);
        premiereLabel.setFont(new Font("Dialog", 2, 12));
        f.add(premiereLabel);

        // 2eme colonne
        JLabel terminaleLabel = new JLabel("Terminale");
        terminaleLabel.setBounds(400, 50, 80, 20);
        terminaleLabel.setFont(new Font("Dialog", 2, 12));
        f.add(terminaleLabel);

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(575, 100, 100, 50);
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getFieldContent(noteFields);
            }
        });
        f.add(submitButton);

        // Okay so I have no idea why but I need to add another component
        // to the frame after adding everything, cuz if I don't then the most
        // recently added component gets fucked up (doesn't show up in the right place),
        // even though when printing its location the debugger says "this is fine"
        JLabel bonusLabel = new JLabel("I am test");
        bonusLabel.setBounds(500, 390, 50, 20);
        f.add(bonusLabel);
        f.remove(bonusLabel);

        f.setVisible(true);
    }

    private void getFieldContent(HashMap<Matiere, ArrayList<JTextField>> noteFields) {
        // The point of this whole program is to predict the required notes to reach a
        // certain threshold. Which means some of the fields will be missing. Store each
        // type in different collections
        ArrayList<Integer> missingCoefs = new ArrayList<Integer>();
        ArrayList<Integer> givenCoefs = new ArrayList<Integer>();
        ArrayList<Float> givenNotes = new ArrayList<Float>();

        // Loop through each label and check if it's valid / fulfilled
        for (ArrayList<JTextField> matiereFields : noteFields.values()) {
            for (JTextField matiereField : matiereFields) {
                Matiere matiereActuelle = getKeyByValue(noteFields, matiereFields);

                // If a single field isn't valid, stop the whole thing
                if (!checkValid(matiereField)) {
                    System.out.println(
                            "L'un des champs de texte de la matière \"" + matiereActuelle.getNom()
                                    + "\" n'est pas valide! Entrez un nombre entre 0 et 20");
                    return;
                }

                // If the subject is split into both years, the notes of those 2 years will have
                // their coef halved (so that the sum of their coef is the one intended for the
                // subject)
                int coef = matiereFields.size() == 1 ? matiereActuelle.getCoefficient()
                        : matiereActuelle.getCoefficient() / 2;

                // If a field isn't filled, go to the next one
                if (!checkFilled(matiereField)) {
                    missingCoefs.add(coef);
                    continue;
                }

                // If the field is both valid and filled, add content to the givenCoefs /
                // givenNotes arrays
                givenCoefs.add(coef);
                givenNotes.add(Float.parseFloat(matiereField.getText()));

            }
        }

        showResult(missingCoefs, givenCoefs, givenNotes);
    }

    private void showResult(ArrayList<Integer> missingCoefs, ArrayList<Integer> givenCoefs,
            ArrayList<Float> givenNotes) {

        // It works! Now I just need to make the interface for it, and it'll be perfect!
        float actualSum = Stats.weightedSum(givenNotes, givenCoefs);
        int[] noteThresholds = { 1000, 1200, 1400, 1600, 1800 };
        String[] mentions = { "Pour avoir ton bac sans mention", "Pour la mention assez bien", "Pour la mention bien",
                "Pour la mention très bien", "Pour les félicitations" };

        if (tempLabels.size() != 0) {
            for (JLabel label : tempLabels) {
                f.remove(label);
            }
        }

        int initialX = 475;
        int initialY = 200;
        int yIncrementation = 65;
        for (int i = 0; i < noteThresholds.length; i++) {
            float requiredNote = Stats.minimumRequiredNote(actualSum, noteThresholds[i], missingCoefs);
            requiredNote = (float) Math.round(requiredNote * 100) / 100;
            String noteMessage = requiredNote <= 0 ? "tu l'as déjà! :D"
                    : requiredNote > 20 ? "impossible ;-;" : String.valueOf(requiredNote);

            JLabel result = new JLabel("<html>" + mentions[i] + ": " + noteMessage);
            result.setBounds(initialX, initialY, 275, 50);
            result.setFont(new Font("Dialog", 1, 15));
            tempLabels.add(result);
            f.add(result);
            initialY += yIncrementation;
        }

        f.setVisible(false);
        JLabel bonusLabel = new JLabel("I am test");
        bonusLabel.setBounds(100, 390, 50, 20);
        f.add(bonusLabel);
        f.remove(bonusLabel);
        f.setVisible(true);

    }

    private boolean checkValid(JTextField noteField) {
        if (!checkFilled(noteField))
            return true;

        float note;
        try {
            note = Float.parseFloat(noteField.getText());
        } catch (Exception e) {
            return false;
        }

        if ((note < 0 || note > 20)) {
            return false;
        }

        return true;
    }

    private boolean checkFilled(JTextField note) {
        if (note.getText().isEmpty())
            return false;
        return true;
    }

    public <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Entry<T, E> entry : map.entrySet()) {
            if (value == entry.getValue()) {
                return entry.getKey();
            }
        }
        return null;
    }
}

class Matiere {
    private int coefficient;
    private String nom;
    private boolean premiere;
    private boolean terminale;

    public Matiere(String nom, int coef, Annees anneesConcernees) {
        this.coefficient = coef;
        this.nom = nom;

        if (anneesConcernees == Annees.PREMIERE || anneesConcernees == Annees.PREMIERE_ET_TERMINALE)
            premiere = true;
        if (anneesConcernees == Annees.TERMINALE || anneesConcernees == Annees.PREMIERE_ET_TERMINALE)
            terminale = true;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(int coef) {
        this.coefficient = coef;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String name) {
        this.nom = name;
    }

    public boolean getPremiere() {
        return premiere;
    }

    public void setPremiere(boolean value) {
        premiere = value;
    }

    public boolean getTerminale() {
        return terminale;
    }

    public void setTerminale(boolean value) {
        terminale = value;
    }
}