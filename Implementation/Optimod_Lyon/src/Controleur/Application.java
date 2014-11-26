package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import Modele.DataWareHouse;
import Outils.*;
import Vue.Fenetre;
import Vue.VueNoeud;

/**
 * 
 */
public class Application implements MouseListener, ActionListener{

	private DataWareHouse modele;
	private Fenetre vue;
	private Vector<Action> listeAnnulation;
	private Vector<Action> listeExecution;
	private XMLhandler outilXML;
	
    public Application(Fenetre vue, DataWareHouse modele) {
    	this.listeAnnulation = new Vector<Action>();
    	this.listeExecution = new Vector<Action>();
    	this.outilXML = new XMLhandler();
    	this.modele = modele;
    	this.vue = vue; 
    }

    public Application()
    {
    	
    }
    	
    private void chargerDemandeLivraison() {

    	File fichierData = outilXML.selectXML();
        if (fichierData != null) {
            try {
            	outilXML.checkXML(fichierData.getAbsolutePath(), Proprietes.PATH_XSD_DL);
                // creation d'un constructeur de documents a l'aide d'une fabrique
               DocumentBuilder constructeur = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
               // lecture du contenu d'un fichier XML avec DOM
               Document document = constructeur.parse(fichierData);
               Element racine = document.getDocumentElement();
               
               // Initialiser les donn�es
                  modele.initLivraison(racine);

           // todo : traiter les erreurs
           } catch (ParserConfigurationException pce) {
               System.out.println("Erreur de configuration du parseur DOM");
               System.out.println("lors de l'appel a fabrique.newDocumentBuilder();");
           } catch (SAXException se) {
               System.out.println("Erreur lors du parsing du document");
               System.out.println("lors de l'appel a construteur.parse(xml)");
           } catch (IOException ioe) {
               System.out.println("Erreur d'entree/sortie");
               System.out.println("lors de l'appel a construteur.parse(xml)");
           }
       } 
    }

    private void chargerPlan() {

    	File fichierData = outilXML.selectXML();
        if (fichierData != null) {
            try {
            	outilXML.checkXML(fichierData.getAbsolutePath(), Proprietes.PATH_XSD_PLAN);
                // creation d'un constructeur de documents a l'aide d'une fabrique
               DocumentBuilder constructeur = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
               // lecture du contenu d'un fichier XML avec DOM
               Document document = constructeur.parse(fichierData);
               Element racine = document.getDocumentElement();

                   // appel des initialiseurs
            	   modele.initDataPlan(racine);

           // todo : traiter les erreurs
           } catch (ParserConfigurationException pce) {
               System.out.println("Erreur de configuration du parseur DOM");
               System.out.println("lors de l'appel a fabrique.newDocumentBuilder();");
           } catch (SAXException se) {
               System.out.println("Erreur lors du parsing du document");
               System.out.println("lors de l'appel a construteur.parse(xml)");
           } catch (IOException ioe) {
               System.out.println("Erreur d'entree/sortie");
               System.out.println("lors de l'appel a construteur.parse(xml)");
           }
       } 
    }

    /**
     * 
     */
    public void gererCommande(String commande) {
        try {
				switch (commande)
				{
				case Proprietes.AJOUTER_LIVRAISON :
					ActionAjouterLivraison action = new ActionAjouterLivraison();
					action.Executer();
					this.listeExecution.addElement(action);
					break;
				case Proprietes.CALC_TOURNEE :
					break;
				case Proprietes.SUPP_LIVRAISON :
					ActionSupprimerLivraison action1 = new ActionSupprimerLivraison();
					action1.Executer();
					this.listeExecution.addElement(action1);
					break;
				case Proprietes.CHARGER_PLAN :
					this.chargerPlan();
					break;
				case Proprietes.CHARGER_LIVRAISON : 
					this.chargerDemandeLivraison();
					break;
				case Proprietes.UNDO :
					Action actionAnnulable = this.listeExecution.lastElement();
					actionAnnulable.Annuler();
					this.listeExecution.removeElementAt(listeExecution.size());
					listeAnnulation.addElement(actionAnnulable);
					break;
				case Proprietes.REDO : 
					Action actionAnnulee = listeAnnulation.lastElement();
					actionAnnulee.Executer();
					listeAnnulation.removeElementAt(listeAnnulation.size());
					listeExecution.addElement(actionAnnulee);
					break;
				case Proprietes.SAVE:
					break;
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * 
     */
    public void initApplication() {
        // TODO implement here
    }
    
	/**
	 * @return the listeAnnulation
	 */
	public Vector<Action> getListeAnnulation() {
		return listeAnnulation;
	}

	/**
	 * @return the listeExecution
	 */
	public Vector<Action> getListeExecution() {
		return listeExecution;
	}

	/**
	 * @return the modele
	 */
	public DataWareHouse getModele() {
		return modele;
	}
	

	
	//-------------------------------------Mouse Listener--------------------------------------------//
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		boolean selected = false; 
		
		for(VueNoeud a : vue.getPlan().getListeVueNoeuds())
		{
			if(a.clickDessus(e.getX(), e.getY()))
			{
				a.selected = true;
				selected = true; 
				vue.logText("Clique sur X : " + a.getX() + " Y : " + a.getY());
			}else
			{
				a.selected = false;
			}
		}
		
		vue.getPlan().repaint();
		if(!selected)
			vue.logText("Clique sur autre chose qu'un noeud");
		return;
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == vue.getBtnChargerPlan()){
			gererCommande(Proprietes.CHARGER_PLAN);
			vue.chargerPlan(modele.getPlanApp());
			vue.repaint();
			vue.logText("Plan chargé");
		}
		
	}


}