package util;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

/**
 * Create a sinner that accepts only numbers
 * @author macbook
 * source : http://stackoverflow.com/questions/9778958/make-jspinner-only-read-numbers-but-also-detect-backspace/9779203#9779203
 */
public class IntegerSpinner {
		public static JSpinner makeIntegerSpinner( int value, int minimum, int maximum, int stepSize) {
		    JSpinner spinner = new JSpinner(new SpinnerNumberModel(value,minimum,maximum,stepSize));
			JSpinner.NumberEditor jsEditor = (JSpinner.NumberEditor) spinner.getEditor();
            final Document jsDoc = jsEditor.getTextField().getDocument();
            if (jsDoc instanceof PlainDocument) {
                AbstractDocument doc = new PlainDocument() {

                  //  private static final long serialVersionUID = 1L;

                    @Override
                    public void setDocumentFilter(DocumentFilter filter) {
                        if (filter instanceof MyDocumentFilter) {
                            super.setDocumentFilter(filter);
                        }
                    }
                };
                doc.setDocumentFilter(new MyDocumentFilter());
                jsEditor.getTextField().setDocument(doc);
            }
            jsEditor.getTextField().setValue(value); // workaround car le spinner ne prend pas la valeur par defaut selon le layout
            return spinner;
		}
		
		
		 private static class MyDocumentFilter extends DocumentFilter {

		        @Override
		        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
		            if (stringContainsOnlyDigits(string)) {
		                super.insertString(fb, offset, string, attr);
		            }
		        }

		        @Override
		        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
		            super.remove(fb, offset, length);
		        }

		        @Override
		        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
		            if (stringContainsOnlyDigits(text)) {
		                super.replace(fb, offset, length, text, attrs);
		            }
		        }

		        private boolean stringContainsOnlyDigits(String text) {
		            for (int i = 0; i < text.length(); i++) {
		                if (!Character.isDigit(text.charAt(i))) {
		                	return false;
		                }
		            }

		            return true;
		        }
		    }

}
