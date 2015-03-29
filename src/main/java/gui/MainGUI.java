package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import main.Main;
import database.DBConnect;

public class MainGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -68866897398811238L;

	public static void main(String[] args) {
		
		@SuppressWarnings("unused")
		MainGUI frameTabel = new MainGUI();
	}

	private JButton connexion = new JButton("Connect");
	//private String value=null;
	private JPanel panelAuthentification = new JPanel();
	private JPanel panelPrincipal = new JPanel();
	private JPanel panelTable = new JPanel();
	private JTextField txtUsername = new JTextField(15);
	private JTextField txtUrl = new JTextField(30);
	private JTextField txtFenetre = new JTextField(30);
	private JPasswordField pwd = new JPasswordField(15);
	private JLabel usernameLabel = new JLabel("User: ");
	private JLabel pwdLabel = new JLabel("Pwd: ");
	private JLabel urlLabel = new JLabel("URL: ");
	private JButton prev;
	private JButton run=new JButton("Run");
	private JButton nextwindow=new JButton("Next Window");

	//private JButton liretable;
	private JButton next;
	private String texte;
	private JScrollPane tableContainer=null;
	private JScrollPane containerInformation=null;
	//private JComboBox liste=null;
	//private  Databaseconnexion conect;
	private int debutParcours=0;
	private int finParcours=0;
	//private java.util.List<String> searchTabledoublon;
	private int nextValue=0;
	
	private static int start = 0;
	
	//private static int inc=0;
	
	private ActionListener listener;
	private JTextArea  InformationTextArea=new JTextArea();
	private JPanel displayTuples=new JPanel();
	private JPanel panelPagination=new JPanel();

	//constructor
	public MainGUI(){
		
		super("Tweets Mining");
	
		setSize(1350,750);
		//setLocation(500,280);
		
		//initialisation du frame
		init();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		//
		actionlogin();
	}
	
	/**
	 * 
	 */
	public void init(){
		
		Listener();
		
		//elimination du panel par défaut du frame
		panelAuthentification .setLayout (null); 
		
		//Positionnement des éléments de connexion
		usernameLabel.setBounds(25,30,350,50);
		pwdLabel.setBounds(25,100,350,50);
		urlLabel.setBounds(25,170,350,50);

		txtUrl.setBounds(65,170,300,35);
		txtUrl.addActionListener(listener);
		txtUrl.setActionCommand("url");
		txtUrl.setText("jdbc:mysql://localhost/tweets");
		txtUrl.setEditable(false);		
		
		//action 
		
		txtUsername.setBounds(65,30,300,35);
		txtUsername.addActionListener(listener);
		txtUsername.setActionCommand("root");		
		
		pwd.setBounds(65,100,300,35);
		pwd.addActionListener(listener);
		pwd.setActionCommand("pass");		
		
		connexion.setBounds(150,250,95,40);
		connexion.addActionListener(listener);
		connexion.setActionCommand("seconnecter");

		usernameLabel.setForeground(Color.WHITE);
		
		urlLabel.setForeground(Color.WHITE);
		pwdLabel.setForeground(Color.WHITE);		

		//ajout des élements de connexion

		panelAuthentification .add(connexion);
		panelAuthentification .add(pwdLabel);
		panelAuthentification .add(usernameLabel);
		panelAuthentification .add(txtUsername);
		panelAuthentification .add(urlLabel);
		panelAuthentification .add(txtUrl);
		panelAuthentification .add(pwd);
		
		//Suppression des Layout et ajout de couleur du panel des Tables
		
		//liste = new JComboBox();
	    //liste.setBounds(35,35,200,30);
	    //JPanel pan=new JPanel();
	    
	    nextwindow.setBounds(15,100,170,30);
	    nextwindow.setActionCommand("fenetrenext");
	    nextwindow.addActionListener(listener);
	    nextwindow.setEnabled(false) ; 
	     
	    run.setBounds(190,100,180,30);
	    run.setActionCommand("run");
	    run.addActionListener(listener);
	    run.setEnabled(false) ; 
	    JLabel label = new JLabel("Taille Fenêtre: ");
	    txtFenetre.setBounds(115,30,200,35);
	    label.setBounds(20,25,350,50);
	    txtFenetre.setActionCommand("fenetrevalue");
	    txtFenetre.addActionListener(listener);
	   
	    panelTable.add(label);
	    panelTable.add(txtFenetre);
	    panelTable.add(nextwindow);
	    panelTable.add(run);
	   
		panelTable.setLayout(null);
		panelTable.setBackground(Color.LIGHT_GRAY);


		panelPrincipal=new JPanel();
		
		panelPrincipal.setLayout (null);
		
		panelPrincipal.add(panelAuthentification);
		panelPrincipal.setBackground(Color.BLACK);
		
		InformationTextArea.setBounds(40,500,400,200);
		InformationTextArea.setEditable(false);
		
		containerInformation=new JScrollPane(InformationTextArea);
		containerInformation.setBounds(40,500,400,200);
		displayTuples.setBounds(500,10,820,690);
		displayTuples.setBackground(Color.WHITE);
		displayTuples.setBorder(BorderFactory.createTitledBorder("Itemsets fréquents"));
		
		panelTable.setBounds(40,330,400,150);
		panelPrincipal.add(panelTable);
		panelPrincipal.add(containerInformation);
		panelPrincipal.add(displayTuples);
		panelAuthentification.setBounds(40,15,400,300);
		panelAuthentification.setBackground(Color.GRAY);

		this.getContentPane().add(panelPrincipal);
		
		//titre des panels

		TitledBorder titled=new TitledBorder("Connexion");
		titled.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		titled.setTitleColor(Color.RED);

		TitledBorder titledinfo=new TitledBorder("Informations");
		titledinfo.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		titledinfo.setTitleColor(Color.RED);
		TitledBorder titledtable=new TitledBorder("Paramètre ItemSet Fréquents");
		titledtable.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		titledtable.setTitleColor(Color.RED);
		
		panelAuthentification.setBorder(titled);
		
		InformationTextArea.setBorder(titledinfo);
		
		panelTable.setBorder(titledtable);
		
		//actions
		
		Initaction();
		
		//boutons de pagination des tuples
		 
		panelPagination.add(prev);
		panelPagination.add(next);
		displayTuples.setLayout(new BorderLayout());
		displayTuples.add(panelPagination, BorderLayout.SOUTH);
	}//end init()
	
	/**
	 * regroupe l'ensemble des listener
	 */
	public void Listener(){
		listener= new ActionListener(){

		    public void actionPerformed(ActionEvent e) {
		        String command = e.getActionCommand();
		        int v=0;
		      
		        if(command.equals("next")){
		        	next();
		        }		        

	            if(command.equals("prev")){
	            	previous();
	            }
		           
	            if(command.equals("fenetrenext")){
	            	
	            	int  val= Integer.parseInt(txtFenetre.getText());
	            	start=start +Integer.parseInt(txtFenetre.getText());
	            	
	            	@SuppressWarnings("unused")
					Main mymain=new Main(val,start,InformationTextArea);	                  
	            }
	          
	            if(command.equals("run")){	
	            	
	            	int  val= Integer.parseInt(txtFenetre.getText());
	            	@SuppressWarnings("unused")
					Main mymain=new Main(val,0,InformationTextArea);
	            	
	        		String ligne;
	        		//int freqItems; //freqItems: est la frequence de l'items- nombre de fois qu'il apparait
	        		String myoutput ="./output_"+start+".arff";
	        		
                    //String[] colNames = {"Itemset", "Support"};
                    JTable table = new JTable();
                    
                    DefaultTableModel  model = new DefaultTableModel();
                    model.addColumn("Itemset");
                    model.addColumn("Support");
                   
                    /** lecture du fichier de resultat et chargement sur la table pour visualisation **/
                    
	        		BufferedReader br=null;
					try {
						br = new BufferedReader(new FileReader(myoutput));
					}
					catch(FileNotFoundException e2){						
						e2.printStackTrace();
					}
	        		//int i=0,j=0;
	        		
	        		try{
						while((ligne = br.readLine()) != null){
							String words[] = ligne.split(":");
							model.addRow(new Object[]{words[0],words[1]});										
						}
						table.setModel(model);
					}
	        		catch (IOException e1){						
						e1.printStackTrace();
					}
	        		
	        		if(tableContainer!=null)
	        		displayTuples.remove(tableContainer);
	                tableContainer = new JScrollPane(table);
	                
	                displayTuples.add(tableContainer, BorderLayout.CENTER);
	                displayTuples.revalidate();
	                txtFenetre.setEditable(false);
	        		  
	            }//end command=run        
	            
	            
	            /* if(command.equals("run")){
	            	 value=(String)liste.getSelectedItem();
	            	 JTable table=null;
	            	 nextvalue=1;
		        		try{
		        			table = new JTable(buildTableModel());
		        		}catch (SQLException op) {      			
		        			
		        			op.printStackTrace();
		        		}
		        		if(tableContainer!=null)
		        		displayTuples.remove(tableContainer);
		                tableContainer = new JScrollPane(table);		                
		                displayTuples.add(tableContainer, BorderLayout.CENTER);
		                displayTuples.revalidate();		          
	            }*/	            
	            
		        if(command.equals("seconnecter")){
		        	String passf=new String(pwd.getPassword());
		        	
		        	if(txtUrl.getText().isEmpty() || txtUsername.getText().isEmpty()){
		        		InformationTextArea.append("Remplissez les champs necessaire à la connexion...\n");		        	
		        	}
		        	else{
		        		DBConnect connecte= new DBConnect(txtUrl.getText(),txtUsername.getText(),passf,InformationTextArea);	        		 
		        		if(connecte.connexionsucced()){		        			 
		        			InformationTextArea.append("\n connexion reussie...\n");
		        			run.setEnabled(true) ;
		        			nextwindow.setEnabled(true);		        	     
		        		}		        		
		        	}
		        }//end command=seconnecter
		        
		        if(v==1){
		        	if(txtUrl.getText().isEmpty() || txtUsername.getText().isEmpty()){
		        		InformationTextArea.append("Remplissez les champs necessaire à la connexion...\n");		        	
		        	}
		        	else{		        		
		        		
		        	 //conect=new Databaseconnexion(txturl.getText(),"com.mysql.jdbc.Driver",txtroot.getText(),facteurSimilarite.getText(),InformationTextArea);		        		
		    	     //Object[] elements =new Object[conect.getTablesName().size()]; 
		    	        
		    	       /*for(int i=0;i<conect.getTablesName().size();i++)
		    	        {		    	        	
		    	        	elements[i]=conect.getTablesName().get(i);
		    	        }*/		    	       
		    	      
		    	        nextwindow.setEnabled(true);
		    	        run.setEnabled(true);		    	        
		        		InformationTextArea.append("\n Connexion reussi...\n");
		    	        InformationTextArea.append("La liste des tables de la base de données renseigné est disponible,\n Dans la liste déroulante du panel  <<Recherche des doublons>> Veuillez cliqué dessus \n" +
		    	        		"Pour voir les tables...");		    	       			
		        	}		        	
		        }//end if v=1
		     
		    }//end actionPerformed
		}; //end listener= new ActionListener()
	}//end Listener()
	
	/**
	 * 
	 */
	public void Initaction(){
		 next = new JButton();
	     next.setPreferredSize(new Dimension(25, 25));
	     next.setIcon(new ImageIcon(getClass().getResource("/images/next.png")));
	     next.setBorderPainted(false);
	     next.setFocusPainted(false);
	     next.setActionCommand("next");
	     next.addActionListener(listener);
	     next.setContentAreaFilled(false);
	     
		 prev = new JButton();
	     prev.setPreferredSize(new Dimension(25, 25));
	     prev.setIcon(new ImageIcon(getClass().getResource("/images/prev.png")));
	     prev.setBorderPainted(false);
	     prev.setFocusPainted(false);
	     prev.setActionCommand("prev");
	     prev.addActionListener(listener);
	     prev.setContentAreaFilled(false);
	}

	/**
	 * 
	 */
	public void actionlogin(){		
		connexion.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				
				@SuppressWarnings("unused")
				String puname = txtUsername.getText();
			}
		});
	}

	/**
	 * fonction qui fait l'action pour visualiser la page suivante
	 * ATTENTION : pas encore implementé pour ce projet
	 */
	@SuppressWarnings("null")
	public void next(){
		
		debutParcours=finParcours;
	    finParcours=finParcours+50;
	    JTable table=null;
		if(nextValue==1){
			 JOptionPane.showMessageDialog(this, "Chargement des block suivants", 
		       "le titre",JOptionPane.INFORMATION_MESSAGE);
			 nextValue=0;
		}
		try{			
			//table = new JTable(conect.buildTableModel(value,debutparcours,finparcours));
		}
		catch (Exception e) {			
			e.printStackTrace();
		}
		
		if(table.getRowCount()==0){}
		else{
			displayTuples.remove(tableContainer);
			tableContainer =null;
		    tableContainer = new JScrollPane(table);	 
		    displayTuples.add(tableContainer, BorderLayout.CENTER);	   
		    displayTuples.revalidate();
	    }	       
	  }//end next()
	   
	/**
	 * fonction qui fait l'action pour visualiser la page précédente
	 * ATTENTION : pas encore implementé pour ce projet
	 */
	public void previous(){
	   	
		debutParcours=debutParcours-50;
		finParcours=finParcours-50;
		if(debutParcours<0)
		   	 debutParcours=0;
		if(finParcours<50)
		   	 finParcours=50;	
		if(debutParcours>=0){
		     JTable table=null;
		     try {
				//	table = new JTable(conect.buildTableModel(value,debutparcours,finparcours));
			 }
		     catch (Exception e){					
				e.printStackTrace();
			 }
			 displayTuples.remove(tableContainer);
		     tableContainer =null;
		     tableContainer = new JScrollPane(table);		       
		     displayTuples.add(tableContainer, BorderLayout.CENTER);		        
		     displayTuples.revalidate();		      
		}	      
	}
	
	/*******************************************************************/
	//		GETTERS ET SETTERS  
	
	public void setTexte(String texte) {
		this.texte = texte;
	}
	public String getTexte() {
		return texte;
	}
	
}//end class GUI