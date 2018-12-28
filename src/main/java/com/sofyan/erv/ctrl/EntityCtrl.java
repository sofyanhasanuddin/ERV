package com.sofyan.erv.ctrl;

import com.sofyan.erv.helper.JpaHelper;
import com.sofyan.erv.helper.RelationHelper;
import com.sofyan.erv.response.EntityInfoResponse;
import com.sofyan.erv.response.Link;
import com.sofyan.erv.response.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
            node.setName( entityInfoResponse.getClassName());
            node.setLabel( entityInfoResponse.getTableName() );

            nodes.add( node );

            entityInfoResponse
                    .getListProperty()
                    .forEach(entityProperty -> {

                        if( !StringUtils.isEmpty( entityProperty.getRelationClass() ) ) {

                            if(entityProperty.isOwnRelation() ) {

                                Link link = new Link();
                                link.setType( entityProperty.getAttributeType().toString() );
                                link.setSource( node.getName() );
                                link.setTarget( entityProperty.getRelationClass() );
                                links.add(link);

                            }

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
