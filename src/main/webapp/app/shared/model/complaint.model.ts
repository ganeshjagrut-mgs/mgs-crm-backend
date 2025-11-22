import dayjs from 'dayjs';
import { ICustomer } from 'app/shared/model/customer.model';
import { IMasterStaticType } from 'app/shared/model/master-static-type.model';
import { IUser } from 'app/shared/model/user.model';
import { ComplaintStatus } from 'app/shared/model/enumerations/complaint-status.model';

export interface IComplaint {
  id?: number;
  complaintNumber?: string;
  complaintDate?: dayjs.Dayjs;
  recordNumbers?: string | null;
  customerContactNumber?: string | null;
  customerContactEmail?: string | null;
  complaintDescription?: string | null;
  expectedClosureDate?: dayjs.Dayjs | null;
  rootCause?: string | null;
  complaintStatus?: keyof typeof ComplaintStatus | null;
  correctiveAction?: string;
  preventiveAction?: string | null;
  complaintClosureDate?: dayjs.Dayjs | null;
  customerName?: ICustomer | null;
  complaintRelatedTo?: IMasterStaticType | null;
  typeOfComplaint?: IMasterStaticType | null;
  complaintRelatedPersons?: IUser[] | null;
}

export const defaultValue: Readonly<IComplaint> = {};
