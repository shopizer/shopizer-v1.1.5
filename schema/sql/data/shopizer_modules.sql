insert into MODULE_CONFIGURATION (
  CONFIGURATION_MODULE,
  CONFIGURATION_KEY,
  COUNTRY_ISO_CODE_2,
  CONFIGURATION_VALUE
) values
('canadapost','packages','CA','01|Flat Rate Envelope;02|Box');

insert into MODULE_CONFIGURATION (
  CONFIGURATION_MODULE,
  CONFIGURATION_KEY,
  COUNTRY_ISO_CODE_2,
  CONFIGURATION_VALUE
) values
('usps','packages','US','01|VARIABLE;02|FLAT RATE BOX;03|FLAT RATE ENVELOPE;04|RECTANGULAR;05|NONRECTANGULAR;06|LG FLAT RATE BOX');


insert into MODULE_CONFIGURATION (
  CONFIGURATION_MODULE,
  CONFIGURATION_KEY,
  COUNTRY_ISO_CODE_2,
  CONFIGURATION_VALUE
) values
('usps','service','US','01|FIRST CLASS;02|PRIORITY;03|EXPRESS;04|EXPRESS SH;05|EXPRESS HFP;06|BPM;07|PARCEL');


insert into MODULE_CONFIGURATION (
  CONFIGURATION_MODULE,
  CONFIGURATION_KEY,
  COUNTRY_ISO_CODE_2,
  CONFIGURATION_VALUE
) values
('fedex','service','XX','FEDEX_GROUND;GROUND_HOME_DELIVERY;FIRST_OVERNIGHT;PRIORITY_OVERNIGHT;INTERNATIONAL_ECONOMY;INTERNATIONAL_FIRST;INTERNATIONAL_PRIORITY;STANDARD_OVERNIGHT;FEDEX_2_DAY;FEDEX_EXPRESS_SAVER;EUROPE_FIRST_INTERNATIONAL_PRIORITY');

insert into MODULE_CONFIGURATION (
  CONFIGURATION_MODULE,
  CONFIGURATION_KEY,
  COUNTRY_ISO_CODE_2,
  CONFIGURATION_VALUE
) values
('fedex','packages','XX','01|FEDEX_TUBE;02|FEDEX_10KG_BOX;03|FEDEX_25KG_BOX;04|FEDEX_BOX;05|FEDEX_PAK;06|FEDEX_ENVELOPE;07|YOUR_PACKAGING');


insert into MODULE_CONFIGURATION (
  CONFIGURATION_MODULE,
  CONFIGURATION_KEY,
  COUNTRY_ISO_CODE_2,
  CONFIGURATION_VALUE
) values
('upsxml','service','XX','01;02;03;07;08;11;12;13;14;54;59;65');

insert into MODULE_CONFIGURATION (
  CONFIGURATION_MODULE,
  CONFIGURATION_KEY,
  COUNTRY_ISO_CODE_2,
  CONFIGURATION_VALUE
) values
('upsxml','packages','XX','02|Customer Package;01|UPS Letter;03|UPS Tube;04|UPS Pak;21|UPS Express Box;24|UPS 25kg Box;10|UPS 10kg box;25|Unknown');

insert into MODULE_CONFIGURATION (
  CONFIGURATION_MODULE,
  CONFIGURATION_KEY,
  COUNTRY_ISO_CODE_2,
  CONFIGURATION_VALUE
) values
('packing-box','box','XX','<table border="0"><tr><td><s:text name="module.box.width" />:</td><td valign="top"><input type="text" name="box_width" size="5"></td></tr><tr><td valign="top"><s:text name="module.box.height" />:</td><td><input type="text" name="box_height" size="5"></td></tr><tr><td valign="top"><s:text name="module.box.length" />:</td><td><input type="text" name="box_length" size="5"></td></tr><tr><td valign="top"><s:text name="module.box.weight" />:<br></td><td><input type="text" name="box_weight" size="5"></td></tr><tr><td valign="top"><s:text name="module.box.maxweight" />:<br></td><td><input type="text" name="box_maxweight" size="5"></td></tr></table>');




INSERT INTO MODULE_CONFIGURATION (
  CONFIGURATION_MODULE,
  CONFIGURATION_KEY,
  COUNTRY_ISO_CODE_2,
  CONFIGURATION_VALUE
) values
('friendslist','fields','XX','{"fields":[{"field":{"type":"text","label":"Invitation type text","name":"invitationType"}},{"field":{"type":"text","label":"Action text","name":"actionText"}}]}');

INSERT INTO MODULE_CONFIGURATION (
  CONFIGURATION_MODULE,
  CONFIGURATION_KEY,
  COUNTRY_ISO_CODE_2,
  CONFIGURATION_VALUE
) values
('productslider','fields','XX','{"fields":[{"field":{"type":"text","label":"Relation type id","name":"relationType"}}]}');

INSERT INTO MODULE_CONFIGURATION (
  CONFIGURATION_MODULE,
  CONFIGURATION_KEY,
  COUNTRY_ISO_CODE_2,
  CONFIGURATION_VALUE
) values
('downloadproducts','fields','XX','{"fields":[{"field":{"type":"text","label":"Relation type id","name":"downloadRelationType"}},{"field":{"type":"radio","label":"Downloads accessibility","name":"downloadsAccess","values":[{"name":"downloadsAccess","value":"All users"},{"name":"downloadsAccess","value":"Fans only"}]}}]}');

INSERT INTO MODULE_CONFIGURATION (
  CONFIGURATION_MODULE,
  CONFIGURATION_KEY,
  COUNTRY_ISO_CODE_2,
  CONFIGURATION_VALUE
) values
('fancontent','fields','XX','{"fields":[{"field":{"type":"text","label":"Label title","name":"fanContentLabelTitle"}}]}');

INSERT INTO MODULE_CONFIGURATION (
  CONFIGURATION_MODULE,
  CONFIGURATION_KEY,
  COUNTRY_ISO_CODE_2,
  CONFIGURATION_VALUE
) values
('anonymouscontent','fields','XX','{"fields":[{"field":{"type":"text","label":"Label title","name":"anonymousContentLabelTitle"}}]}');