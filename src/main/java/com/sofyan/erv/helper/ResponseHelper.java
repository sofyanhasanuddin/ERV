package com.sofyan.erv.helper;

import com.sofyan.erv.response.EntityInfoResponse;
import com.sofyan.erv.response.Link;
import com.sofyan.erv.response.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

public class ResponseHelper {
	
	private ResponseHelper() {}

    public static Map<String,Object> buildResponse(Map<String,EntityInfoResponse> mapEntityWithRelation) {

    	List<Node> nodes = new ArrayList<>();
        List<Link> links = new ArrayList<>();
        List<EntityInfoResponse> list = new ArrayList<>();

        mapEntityWithRelation.forEach((s, entityInfoResponse) -> {

            list.add( entityInfoResponse );

            Node node = new Node();
            node.setName( entityInfoResponse.getClassName());
            node.setLabel( entityInfoResponse.getTableName() );
            node.setId( entityInfoResponse.getId() );

            nodes.add( node );

            entityInfoResponse
                    .getListProperty()
                    .forEach(entityProperty -> {

                        if( !StringUtils.isEmpty( entityProperty.getRelationClass() ) ) {

                            if(entityProperty.isOwnRelation() ) {

                                Link link = new Link();
                                link.setSource( node.getId() );
                                link.setTarget( mapEntityWithRelation.get(entityProperty.getRelationClass()).getId() );
                                link.setType( entityProperty.getAttributeType().toString() );
                                link.setFieldRelation( entityProperty.getName() );

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
