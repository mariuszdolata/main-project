package encore.windows_app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;

import encore.json.Stats;
import encore.json.TicketRepository;

public class WindowAppMain {
	public static File openSelectedFile;
	public static File saveSelectedFile;
	public static JFrame mainFrame;
	public static JTabbedPane tabbedPane;
	public static JPanel panelTicketsStats;
	public static JTextArea ticketStats;
	public static JTextArea ticketInputData;
	public static JScrollPane scroll;
	public static TicketRepository ticketRespository;

	public static void main(String[] args) {
		mainFrame = new JFrame("Statystyki ticketów");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setBounds(100, 100, 1200, 750);
		tabbedPane = new JTabbedPane();
		mainFrame.setJMenuBar(WindowAppMain.menuBarCreator());
		mainFrame.setVisible(true);

	}

	public static JMenuBar menuBarCreator() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menuPlik = new JMenu("Plik");
		JMenuItem menuItemOtworz = new JMenuItem("Otwórz");
		menuPlik.add(menuItemOtworz);
		menuItemOtworz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectOpenFile();
			}
		});
		JMenuItem menuItemZapisz = new JMenuItem("Zapisz");
		menuPlik.add(menuItemZapisz);
		menuItemZapisz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveStatystykaDzienna();
			}
		});
		JMenuItem menuItemWyjdz = new JMenuItem("Wyjdz");
		menuPlik.add(menuItemWyjdz);
		menuBar.add(menuPlik);
		JMenu menuStatystyka = new JMenu("Statystyka");
		JMenuItem menuItemDzienna = new JMenuItem("Dzienna");
		menuStatystyka.add(menuItemDzienna);
		menuItemDzienna.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				createStatystykaDzienna();

			}
		});
		JMenuItem menuItemMiesieczna = new JMenuItem("Miesieczna");
		menuStatystyka.add(menuItemMiesieczna);
		menuItemMiesieczna.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				createStatystykaMiesieczna();

			}
		});
		menuBar.add(menuStatystyka);
		JMenu menuPomoc = new JMenu("Pomoc");
		JMenuItem menuItemPomoc = new JMenuItem("Pomoc");
		menuPomoc.add(menuItemPomoc);
		menuBar.add(menuPomoc);
		return menuBar;
	}

	public static void createStatystykaDzienna() {
		System.out.println("Wejœcie w statystykê dzienn¹");
		if (openSelectedFile == null) {
			selectOpenFile();
		}
		if (openSelectedFile != null) {
			mainFrame.add(tabbedPane);
//			ticketStats=new JTextArea("Statystyka", 10, 25);	
						
			ticketRespository = new TicketRepository();
			ticketRespository.readFromFile(openSelectedFile);
			ticketRespository.showAllStats();
			ticketRespository.printAllGroups();
			ticketRespository.createStats();
			ticketRespository.showAllGroupedStats();
//			ticketStats.setText(ticketRespository.returnAllGroupedStats());
			Iterator <String> iterator = ticketRespository.getGroups().iterator();
			String stat="";
			//Pomocnicza lista statystyk sluzaca do wyswietlenia danych w tabeli
			List<Stats> statystyka= new ArrayList<Stats>();
			List<String> keys = new ArrayList<String>();
			int numberOfCampaign=0;
			while(iterator.hasNext()){
				numberOfCampaign++;
				String key = iterator.next();
				keys.add(key);
				Stats statsTmp = ticketRespository.getMap().get(key);
				statystyka.add(statsTmp);
//				ticketStats.setText(String.format("%-40 %-20", key, "haha"));
			}
			//dwuwymiarowa tablica do prezentacji wynikow
			Object[][] data = new Object[numberOfCampaign][5];
			for(int i=0;i<statystyka.size();i++){
//			for(int i=0;i<10;i++){
				data[i][0]=keys.get(i);
				data[i][1]=statystyka.get(i).getTotal();
				data[i][2]=statystyka.get(i).getLead();
				data[i][3]=statystyka.get(i).getUnsubscribe();
				data[i][4]=statystyka.get(i).getNegative();
			}
			Object[] columnName = new Object[5];
			columnName[0]="Nazwa";
			columnName[1]="Tickety";
			columnName[2]="Leady";
			columnName[3]="Unsuby";
			columnName[4]="Negatywne";
			JTable tableStats=new JTable(data, columnName);
//			tableStats.setAutoResizeMode(JTable.WIDTH);
			tableStats.setAutoCreateRowSorter(true);
			tableStats.setRowHeight(20);
			tableStats.getColumn("Nazwa").setPreferredWidth(250);
			tableStats.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
			
			DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
			rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
			tableStats.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);
			panelTicketsStats = new JPanel();
			tabbedPane.add("statystyka", panelTicketsStats);
			
			panelTicketsStats.add(tableStats.getTableHeader(), BorderLayout.NORTH);
			scroll = new JScrollPane(tableStats);
			scroll.setPreferredSize(new Dimension(600, 500));
			panelTicketsStats.add(scroll, BorderLayout.SOUTH);
//			tabbedPane.add("Statystyka", tableStats);
			
//			tablePanel.add(table, BorderLayout.CENTER);
//			tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);
			mainFrame.setVisible(true);
		}
		else
		{
			JOptionPane.showMessageDialog(null, "nie wybrano pliku z ticketami");
		}
	}

	public static void createStatystykaMiesieczna() {
		System.out.println("Wejœcie w statystykê miesiêczn¹");
	}

	public static void selectOpenFile() {
		System.out.println("Menu->Plik->Otwórz");
		JFileChooser fileChooser = new JFileChooser("F://");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("json files", "json", "txt");
		fileChooser.setFileFilter(filter);
		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			openSelectedFile = fileChooser.getSelectedFile();
			try {
				System.out.println(openSelectedFile.getCanonicalPath().toString());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}
	
	public static void saveStatystykaDzienna(){
		if(ticketRespository!=null){
			System.out.println("Menu->Plik->Zapisz");
			JFileChooser fileChooser = new JFileChooser();

			int returnValue = fileChooser.showSaveDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				saveSelectedFile = fileChooser.getSelectedFile();
				
				try {
					System.out.println(saveSelectedFile.getCanonicalPath().toString());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					BufferedWriter output;
					output = new BufferedWriter(new FileWriter(saveSelectedFile));
					Iterator<String> iterator = ticketRespository.getGroups().iterator();
					while(iterator.hasNext()){
						String key=iterator.next();
						Stats statsTmp=ticketRespository.getMap().get(key);
						//output.write(key+";"+statsTmp.getTotal()+";"+statsTmp.getLead()+";"+statsTmp.getUnsubscribe()+";"+statsTmp.getNegative());
						output.write(String.format("%-40s %-20s %-10s %-13s %-10s %-19s %-10s %-16s %-10s", key, "::  total: ", statsTmp.getTotal(), "leads: ", statsTmp.getLead(), "unsubscribes: ", statsTmp.getUnsubscribe(), " negatives: ", statsTmp.getNegative()));
						output.newLine();
					}
					output.close();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Nie mo¿na zapisaæ do wskazanego pliku","B³¹d pliku",  JOptionPane.ERROR_MESSAGE);
					System.err.println("M: Problem z zapisem statystyk do pliku");
					e.printStackTrace();
				}
				
				
				
			}
		}
		else{
			JOptionPane.showMessageDialog(null, "Nie wczytano pliku z ticketami", "Brak danych", JOptionPane.ERROR_MESSAGE);
		}
	}

}
