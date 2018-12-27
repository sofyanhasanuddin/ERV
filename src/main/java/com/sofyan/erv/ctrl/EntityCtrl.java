package com.sofyan.erv.ctrl;

import com.sofyan.erv.helper.JpaHelper;
import com.sofyan.erv.helper.RelationHelper;
import com.sofyan.erv.response.EntityInfoResponse;
import com.sofyan.erv.response.Link;
import com.sofyan.erv.response.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import javax.persistence.metamodel.Attribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class EntityCtrl {

    @Autowired
    private EntityManager em;

    @GetMapping(path = "/list-entities")
    public Map<String,Object> getEntities() {

        List<Node> nodes = new ArrayList<>();
        List<Link> links = new ArrayList<>();

        //return JpaHelper.getInfo( em );
        List<EntityInfoResponse> list =  RelationHelper.findAllClass("com.sofyan.erv");
        list.forEach(entityInfoResponse -> {

            Node node = new Node();
            node.setId(1);
            node.setLabel( entityInfoResponse.getClassName());
            node.setName( entityInfoResponse.getTableName() );

            nodes.add( node );

            entityInfoResponse
                    .getListProperty()
                    .forEach(entityProperty -> {

                        boolean hasRelation = false;

                        if ( entityProperty.getAttributeType().equals(Attribute.PersistentAttributeType.ONE_TO_ONE) ) {
                            hasRelation = true;
                        }else if ( entityProperty.getAttributeType().equals(Attribute.PersistentAttributeType.MANY_TO_ONE) ) {
                            hasRelation = true;
                        } else if ( entityProperty.getAttributeType().equals(Attribute.PersistentAttributeType.ONE_TO_MANY) ) {
                            hasRelation = true;
                        } else if ( entityProperty.getAttributeType().equals(Attribute.PersistentAttributeType.MANY_TO_MANY) ) {
                            hasRelation = true;
                        }

                        if( hasRelation ) {

                            Link link = new Link();
                            link.setType( entityProperty.getAttributeType().toString() );
                            if(entityProperty.isOwnRelation() ) {
                                link.setSource(node.getName());
                                link.setTarget(entityProperty.getName() );
                            }
                            else {
                                link.setSource(entityProperty.getName());
                                link.setTarget(node.getName());
                            }

                            links.add(link);

                        }

                    });

        });

        Map<String,Object> response = new HashMap<>();
        response.put("entity", list);
        response.put("nodes", nodes);
        response.put("links", links);

        return response;

    }

}
