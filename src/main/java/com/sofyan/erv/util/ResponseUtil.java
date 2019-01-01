package com.sofyan.erv.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.sofyan.erv.response.EntityInfo;
import com.sofyan.erv.response.Link;
import com.sofyan.erv.response.Node;

public class ResponseUtil {

	private ResponseUtil() {}

	public static Map<String, Object> buildResponse(String fileToUpload, String originalPkg) {

		Map<String, EntityInfo> mapEntityWithRelation = RelationUtil.findAllClass(fileToUpload, originalPkg);

		// For holding a specific class that is not include in original package scan
		Map<String, EntityInfo> tempEntityInfos = new HashMap<String, EntityInfo>();

		List<Node> nodes = new ArrayList<>();
		List<Link> links = new ArrayList<>();
		List<EntityInfo> list = new ArrayList<>();

		mapEntityWithRelation.forEach((s, entityInfoResponse) -> {

			list.add(entityInfoResponse);

			Node node = new Node();
			node.setName(entityInfoResponse.getClassName());
			node.setLabel(entityInfoResponse.getTableName());
			node.setId(entityInfoResponse.getId());

			nodes.add(node);

			entityInfoResponse.getListProperty().forEach(entityProperty -> {

				if (!StringUtils.isEmpty(entityProperty.getRelationClass())) {

					if (entityProperty.isOwnRelation()) {

						Link link = new Link();
						link.setSource(node.getId());
						link.setType(entityProperty.getAttributeType().toString());
						link.setFieldRelation(entityProperty.getName());

						if (mapEntityWithRelation.get(entityProperty.getRelationClass()) != null) {
							link.setTarget(mapEntityWithRelation.get(entityProperty.getRelationClass()).getId());

						} else if (tempEntityInfos.get(entityProperty.getRelationClass()) != null) {
							link.setTarget(tempEntityInfos.get(entityProperty.getRelationClass()).getId());

						} else if(entityProperty.getRelationClass() != null) {

							// Get only one class
							EntityInfo tempEir = RelationUtil.findOneClass(fileToUpload,
									entityProperty.getRelationClass());
							link.setTarget(tempEir.getId());
							
							//Create new Node for this shit
							Node tempNode = new Node();
							tempNode.setName(tempEir.getClassName());
							tempNode.setLabel(tempEir.getTableName());
							tempNode.setId(tempEir.getId());

							nodes.add(tempNode);
							
							tempEntityInfos.put(entityProperty.getRelationClass(), tempEir);

						}

						links.add(link);

					}

				}

			});
		});
		
		//Join with exising;
		tempEntityInfos.forEach((s, entityInfoResponse) -> list.add( entityInfoResponse ));

		Map<String, Object> response = new HashMap<>();
		response.put("entity", list);
		response.put("nodes", nodes);
		response.put("links", links);

		return response;

	}

}
