import { IEntityType } from 'app/shared/model/entity-type.model';
import { MasterStaticGroupEditable } from 'app/shared/model/enumerations/master-static-group-editable.model';

export interface IMasterStaticGroup {
  id?: number;
  name?: string;
  description?: string | null;
  uiLabel?: string | null;
  editable?: keyof typeof MasterStaticGroupEditable | null;
  entityType?: IEntityType | null;
}

export const defaultValue: Readonly<IMasterStaticGroup> = {};
