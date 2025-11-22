import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { ICustomer } from 'app/shared/model/customer.model';
import { IMasterStaticType } from 'app/shared/model/master-static-type.model';
import { IPipeline } from 'app/shared/model/pipeline.model';
import { TaskType } from 'app/shared/model/enumerations/task-type.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface ITask {
  id?: number;
  taskType?: keyof typeof TaskType;
  dueDate?: dayjs.Dayjs;
  taskName?: string;
  status?: keyof typeof Status;
  taskCompletionDate?: dayjs.Dayjs | null;
  correlationId?: string | null;
  taskOwner?: IUser | null;
  customer?: ICustomer | null;
  relatedTo?: IMasterStaticType | null;
  pipeline?: IPipeline | null;
}

export const defaultValue: Readonly<ITask> = {};
