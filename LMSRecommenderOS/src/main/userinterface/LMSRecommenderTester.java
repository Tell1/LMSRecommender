package main.userinterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jcolibri.util.FileIO;
import main.domain.StandardLMSRecommender;
import main.foundation.evaluation.LMSRecommenderEvaluater;
import main.foundation.evaluation.LMSRecommenderEvaluater.EvalType;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.WriterAppender;

/**
 * Main tester application GUI that provides the functionality to executes all
 * LMSRecommender implementations. Furthermore it is possible to evaluate the
 * recommender by a 'leave one out' or 'hold out'(Cross-Validation) evaluation.
 * The GUI also shows the different source codes and related documentation each
 * LMSRecommender.
 * 
 * Note: These LMSRecommenders are CBR (Case based Reasoning) recommenders
 * implemented for an e-learning environment like Learning Management Systems
 * (LMS).
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.1
 */
public class LMSRecommenderTester extends JFrame {

	private static final long serialVersionUID = 9207374161965481788L;

	public JList<Object> list;
	public List<ExampleInfo> info;
	public JTabbedPane tabPane;
	public JTextArea displayPane;
	public JButton run;
	public JButton eval;
	public JDialog evalDialog;
	public JButton cVal;

	/**
	 * Constructor that creates the tester using a given config file
	 */
	public LMSRecommenderTester(String configfile) {
		info = parseExampleInfo(configfile);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
		}

		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		this.setSize(screenSize);
		this.setTitle("LMSRecommender Tester");

		JPanel panelUp = new JPanel();
		panelUp.setBorder(BorderFactory
				.createTitledBorder("Available Recommender"));
		JPanel panelDown = new JPanel();
		panelDown.setBorder(BorderFactory.createTitledBorder("Execution log"));

		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelUp,
				panelDown);
		split.setDividerLocation(400);
		this.getContentPane().add(split);

		JPanel topLeft = new JPanel();
		topLeft.setLayout(new BorderLayout());

		Object[] data = new Object[info.size()];
		int i = 0;
		for (ExampleInfo ei : info)
			data[i++] = ei.getName();
		list = new JList<Object>(data);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		JScrollPane listScroller = new JScrollPane(list);

		topLeft.add(listScroller, BorderLayout.CENTER);

		run = new JButton("Execute");

		run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				runExample();
			}
		});

		eval = new JButton("Evaluate");
		eval.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				runEvaluater(EvalType.LEAVE_ONE_OUT);
			}
		});

		cVal = new JButton("Cross-Validation (75%)");
		cVal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				runEvaluater(EvalType.HOLD_OUT);
			}
		});

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BorderLayout(2, 1));
		buttonsPanel.add(run, BorderLayout.WEST);
		buttonsPanel.add(eval, BorderLayout.CENTER);
		buttonsPanel.add(cVal, BorderLayout.EAST);

		topLeft.add(buttonsPanel, BorderLayout.SOUTH);

		panelUp.setLayout(new BorderLayout());
		panelUp.add(topLeft, BorderLayout.WEST);

		tabPane = new JTabbedPane();
		panelUp.add(tabPane, BorderLayout.CENTER);

		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == true)
					return;
				if (list.getSelectedIndex() == -1)
					return;
				int i = list.getSelectedIndex();
				setExample(i);
			}
		});

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		displayPane = new JTextArea();
		displayPane.setEditable(false);
		panelDown.setLayout(new BorderLayout());
		panelDown.add(new JScrollPane(displayPane));

		PrintStream aPrintStream = new PrintStream(new FilteredStream(
				new ByteArrayOutputStream()));
		System.setOut(aPrintStream);
		System.setErr(aPrintStream);

		ConsoleAppender a = (ConsoleAppender) Logger.getRootLogger()
				.getAllAppenders().nextElement();
		Layout l = a.getLayout();
		Logger.getRootLogger().addAppender(new WriterAppender(l, aPrintStream));
		Logger.getRootLogger().removeAppender(a);

	}

	/**
	 * Runs a test in a different thread.
	 */
	public void runExample() {
		run.setEnabled(false);
		try {
			int i = list.getSelectedIndex();
			if (i == -1) {
				run.setEnabled(true);
				return;
			}
			displayPane.setText("");

			ExampleInfo ei = this.info.get(i);

			MethodRunner mr = null;
			Class<?> c = ei.getMainClass();

			Method[] methods = c.getMethods();
			for (Method m : methods)
				if (m.getName().equals("main"))
					mr = new MethodRunner(m);
			if (mr == null)
				return;
			Thread executionThread = new Thread(mr);
			executionThread.start();

		} catch (Exception e) {
			org.apache.commons.logging.LogFactory.getLog(this.getClass())
					.error(e, e);
		}
	}

	public void runEvaluater(EvalType evalType) {
		eval.setEnabled(false);
		cVal.setEnabled(false);
		try {
			int i = list.getSelectedIndex();
			if (i == -1) {
				eval.setEnabled(true);
				cVal.setEnabled(true);
				return;
			}
			displayPane.setText("");

			ExampleInfo ei = this.info.get(i);

			Method meth = ei.getMainClass().getDeclaredMethod("getInstance",
					(Class<?>[]) null);
			Object obj = meth.invoke(null, (Object[]) null);
			StandardLMSRecommender newRec = null;
			if (obj instanceof StandardLMSRecommender) {
				newRec = (StandardLMSRecommender) obj;
			}
			MethodRunnerWithArgs mr = null;
			Class<?> c = LMSRecommenderEvaluater.class;
			Method[] methods = c.getMethods();
			for (Method m : methods) {
				if (m.getName().equals("evalRecommender")) {
					mr = new MethodRunnerWithArgs(m, newRec, evalType);
				}
			}
			if (mr == null)
				return;
			Thread executionThread = new Thread(mr);
			executionThread.start();

		} catch (Exception e) {
			org.apache.commons.logging.LogFactory.getLog(this.getClass())
					.error(e, e);
		}
	}

	/**
	 * Thread for running a test
	 * 
	 * @author Juan A. Recio-Garcia
	 * @version 1.0
	 */
	class MethodRunner implements Runnable {
		Method method;

		MethodRunner(Method m) {
			this.method = m;
		}

		@Override
		public void run() {
			Object[] args = new Object[1];
			args[0] = null;
			try {
				method.invoke(null, args);
			} catch (Exception e) {
				org.apache.commons.logging.LogFactory.getLog(this.getClass())
						.error(e);
				e.printStackTrace();
			}
			org.apache.commons.logging.LogFactory.getLog(
					LMSRecommenderTester.class).info("Test execution finished");
			run.setEnabled(true);
		}

	}

	class MethodRunnerWithArgs implements Runnable {
		Method method;
		Object arg1;
		Object arg2;

		MethodRunnerWithArgs(Method m, Object arg1, Object arg2) {
			this.method = m;
			this.arg1 = arg1;
			this.arg2 = arg2;
		}

		@Override
		public void run() {
			try {
				method.invoke(null, arg1, arg2);
			} catch (Exception e) {
				org.apache.commons.logging.LogFactory.getLog(this.getClass())
						.error(e);
				e.printStackTrace();
			}
			org.apache.commons.logging.LogFactory.getLog(
					LMSRecommenderTester.class).info("Test execution finished");
			eval.setEnabled(true);
			cVal.setEnabled(true);
		}
	}

	/**
	 * Shows an example in the window.
	 */
	public void setExample(int i) {
		try {
			ExampleInfo ei = this.info.get(i);
			tabPane.removeAll();

			JEditorPane ep = new JEditorPane();
			ep.setContentType("text/html");
			ep.setText("<font face=\"verdana, arial, helvetica\"><b>"
					+ ei.getDescription() + "</b></font>");
			ep.setEditable(false);
			tabPane.add("Description", new JScrollPane(ep));

			ep = new JEditorPane(ei.getSource());
			ep.setContentType("text/html");
			ep.setEditable(false);
			tabPane.add("Source", new JScrollPane(ep));

			for (URL url : ei.getDoc()) {
				int b = url.getFile().lastIndexOf('/') + 1;
				int e = url.getFile().lastIndexOf(".html");
				String name = url.getFile().substring(b, e);
				if (name.equals("package-summary")) {
					String aux = url.getFile().substring(0, b - 1);
					int x = aux.lastIndexOf('/') + 1;
					name = aux.substring(x);
				}
				tabPane.add("[Doc]" + name, new HTMLBrowser(url));
			}
		} catch (IOException e) {
			org.apache.commons.logging.LogFactory.getLog(this.getClass())
					.error(e, e);
		}
	}

	/**
	 * Main method
	 */
	public static void main(String[] args) {
		LMSRecommenderTester main = new LMSRecommenderTester(
				"main/userinterface/examples.config");
		main.setVisible(true);

	}

	/**
	 * Parses the information in the config file
	 */
	public List<ExampleInfo> parseExampleInfo(String file) {
		ArrayList<ExampleInfo> info = new ArrayList<ExampleInfo>();

		try {
			BufferedReader br = null;
			br = new BufferedReader(new InputStreamReader(FileIO.findFile(file)
					.openStream()));
			String line = "";
			ExampleInfo ei;
			while ((line = br.readLine()) != null) {
				ei = new ExampleInfo();
				ei.setName(line);
				ei.setDescription(br.readLine());
				ei.setMainClass(Class.forName(br.readLine()));
				ei.setSource(FileIO.findFile(br.readLine()));
				while (!(line = br.readLine()).equals("<example>"))
					ei.addDoc(FileIO.findFile(line));
				info.add(ei);
			}
			br.close();
		} catch (Exception e) {
			org.apache.commons.logging.LogFactory.getLog(this.getClass())
					.error(e, e);
		}
		return info;
	}

	/**
	 * Bean that stores the information in the config file.
	 * 
	 * @author Juan A. Recio-Garcia
	 * @version 1.0
	 * 
	 */
	class ExampleInfo {
		String name;
		String description;
		Collection<URL> doc;
		URL source;
		Class<?> mainClass;

		public ExampleInfo() {
			doc = new ArrayList<URL>();
		}

		public ExampleInfo(String name, String description, Collection<URL> doc) {
			super();
			this.name = name;
			this.description = description;
			this.doc = doc;
		}

		/**
		 * @return Returns the mainClass.
		 */
		public Class<?> getMainClass() {
			return mainClass;
		}

		/**
		 * @param mainClass
		 *            The mainClass to set.
		 */
		public void setMainClass(Class<?> mainClass) {
			this.mainClass = mainClass;
		}

		/**
		 * @return Returns the source.
		 */
		public URL getSource() {
			return source;
		}

		/**
		 * @param source
		 *            The source to set.
		 */
		public void setSource(URL source) {
			this.source = source;
		}

		/**
		 * @return Returns the description.
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @param description
		 *            The description to set.
		 */
		public void setDescription(String description) {
			this.description = description;
		}

		/**
		 * @return Returns the doc.
		 */
		public Collection<URL> getDoc() {
			return doc;
		}

		/**
		 * @param doc
		 *            The doc to set.
		 */
		public void setDoc(Collection<URL> doc) {
			this.doc = doc;
		}

		public void addDoc(URL url) {
			this.doc.add(url);
		}

		/**
		 * @return Returns the name.
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name
		 *            The name to set.
		 */
		public void setName(String name) {
			this.name = name;
		}

	}

	/**
	 * A HTML browser panel
	 * 
	 * @author Juan A. Recio-Garcia
	 * @version 1.0
	 * 
	 */
	class HTMLBrowser extends JPanel {
		private static final long serialVersionUID = 1L;

		JEditorPane doc;
		URL originalURL;

		public HTMLBrowser(URL file) {
			originalURL = file;
			JPanel top = new JPanel();
			top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));

			JButton prev = new JButton("Back");
			prev.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						doc.setPage(originalURL);
					} catch (IOException e1) {
						org.apache.commons.logging.LogFactory.getLog(
								this.getClass()).error(e1, e1);
					}
				}
			});

			JLabel urlInfo = new JLabel(file.toString());

			top.add(prev);
			top.add(Box.createHorizontalGlue());
			top.add(urlInfo);

			doc = new JEditorPane();
			doc.setContentType("text/html");
			doc.setEditable(false);
			doc.addHyperlinkListener(new HyperlinkListener() {

				@Override
				public void hyperlinkUpdate(HyperlinkEvent e) {
					try {
						if (e.getEventType().equals(
								HyperlinkEvent.EventType.ACTIVATED))
							doc.setPage(e.getURL());

					} catch (IOException e1) {
						org.apache.commons.logging.LogFactory.getLog(
								this.getClass()).error(e1, e1);
					}
				}

			});
			try {
				doc.setPage(file);
			} catch (IOException e) {
				org.apache.commons.logging.LogFactory.getLog(this.getClass())
						.error(e, e);
			}

			this.setLayout(new BorderLayout());
			this.add(top, BorderLayout.NORTH);
			this.add(new JScrollPane(doc), BorderLayout.CENTER);
		}

	}

	/**
	 * This class redirects System.out to the log text area.
	 * 
	 * @author Juan A. Recio-Garcia
	 * @version 1.0
	 */
	class FilteredStream extends FilterOutputStream {
		public FilteredStream(OutputStream aStream) {
			super(aStream);
		}

		@Override
		public void write(byte b[]) throws IOException {
			String aString = new String(b);
			displayPane.append(aString);
			displayPane.setCaretPosition(displayPane.getDocument().getLength());
		}

		@Override
		public void write(byte b[], int off, int len) throws IOException {
			String aString = new String(b, off, len);
			displayPane.append(aString);
			displayPane.setCaretPosition(displayPane.getDocument().getLength());
		}
	}

}
