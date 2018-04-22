
%dw 1.0
%output application/java
---
{
	encoding: "UTF-8",
	maxBatchSize: 1,
	sobjects: [{
		activateable: true,
		createable: true,
		custom: true,
		customSetting: true,
		deletable: true,
		deprecatedAndHidden: true,
		feedEnabled: true,
		keyPrefix: "key_",
		label: "Label1",
		labelPlural: "Labels1",
		layoutable: true,
		mergeable: true,
		name: "LabelName1",
		queryable: true,
		replicateable: true,
		retrieveable: true,
		searchable: true,
		triggerable: true,
		undeletable: true,
		updateable: true
	},
	{
		activateable: true,
		createable: true,
		custom: true,
		customSetting: true,
		deletable: true,
		deprecatedAndHidden: true,
		feedEnabled: true,
		keyPrefix: "key_",
		label: "Label2",
		labelPlural: "Labels2",
		layoutable: true,
		mergeable: true,
		name: "LabelName2",
		queryable: true,
		replicateable: true,
		retrieveable: true,
		searchable: true,
		triggerable: true,
		undeletable: true,
		updateable: true
	}]
} as :object {
	class : "com.sforce.soap.partner.DescribeGlobalResult"
}