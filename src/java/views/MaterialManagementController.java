/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package views;

import domain.Material;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import services.MaterialService;

/**
 *
 * @author 687159
 */
public class MaterialManagementController extends HttpServlet 
{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        MaterialService ms = new MaterialService();
        ArrayList<Material> materials = ms.getAllMaterials();
        
        String action = request.getParameter("action");
        if (action != null && action.equals("edit")) 
        {
            String matID = request.getParameter("materialSelected"); 
            int materialID = Integer.parseInt(matID);
            try
            {
                for(Material material: materials)
                {
                    if(material.getMaterialId().equals(materialID))
                    {
                        request.setAttribute("material", material);
                    }
                }
            } 
            catch (Exception ex) 
            {
                Logger.getLogger(MaterialManagementController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        request.getRequestDispatcher("/WEB-INF/materialMgmt.jsp").forward(request, response); 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        String action = request.getParameter("action");
        int materialID;
        String materialName = request.getParameter("materialName");
        String materialDesc = request.getParameter("materialDesc");

        MaterialService ms = new MaterialService();

        try 
        {
            switch(action)
            {
                case "add":
                    if(!(materialName == null || materialName.equals("")) 
                            && !(materialDesc == null || materialDesc.equals("")))
                    {          
                        ArrayList<Material> materials = ms.getAllMaterials();
                        Material mat =  ms.createMaterial();
                        materials.add(mat);
                        request.setAttribute("materials", materials);
                        request.setAttribute("addM", "New Material added.");
                        getServletContext().getRequestDispatcher("/WEB-INF/materialMgmt.jsp").forward(request, response);   
                    }
                    else
                    {
                        request.setAttribute("errorM", "Please enter the required fields.");
                        getServletContext().getRequestDispatcher("/WEB-INF/materialMgmt.jsp").forward(request, response);
                    }
                    break;
                case "edit":
                    if(!(materialName == null || materialName.equals("")) 
                        && !(materialDesc == null || materialDesc.equals("")))
                    {
                        for(Material editMaterial: ms.getAllMaterials())
                        {
                            System.out.println(editMaterial.getMaterialId());
                            if(editMaterial.getName().equals(materialName))
                            {
                                System.out.println(editMaterial.getMaterialId());
                                System.out.println("Finding Material");
                                ms.updateMaterial(materialName, materialDesc, editMaterial.getMaterialId());
                                System.out.println("Found Material");
                            }
                        }
                        System.out.println("Done updating");
                        request.setAttribute("editM", "Material has been updated.");
                        request.getRequestDispatcher("/WEB-INF/materialMgmt.jsp").forward(request, response);
                        return;
                    }
                    else
                    {
                        request.setAttribute("errorM", "Please enter all of the required fields.");
                        request.getRequestDispatcher("/WEB-INF/materialMgmt.jsp").forward(request, response);
                    }

                    break;
                    case "delete":
                        String matID = request.getParameter("materialID");
                        materialID = Integer.parseInt(matID);
                        int material = ms.deleteMaterial(materialID);
                        if (material == 0)
                        {
                            request.setAttribute("errorDeleteM", "Can't delete this material.");
                            request.getRequestDispatcher("/WEB-INF/materialMgmt.jsp").forward(request, response);
                        }
                        else
                        {
                            ms.deleteMaterial(materialID);
                            request.setAttribute("deleteM", "Material has been deleted.");
                            request.getRequestDispatcher("/WEB-INF/materialMgmt.jsp").forward(request, response); 
                            break;

                        }
                    default:
                        break;
            }
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(MaterialManagementController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
