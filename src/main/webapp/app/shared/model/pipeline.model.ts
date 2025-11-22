import { IPipelineTag } from 'app/shared/model/pipeline-tag.model';
import { ITask } from 'app/shared/model/task.model';
import { IUser } from 'app/shared/model/user.model';
import { ICustomer } from 'app/shared/model/customer.model';
import { IMasterStaticType } from 'app/shared/model/master-static-type.model';
import { ISubPipeline } from 'app/shared/model/sub-pipeline.model';

export interface IPipeline {
  id?: number;
  pipelineName?: string;
  totalAmount?: number | null;
  noOfSamples?: number | null;
  correlationId?: string | null;
  pipelineTags?: IPipelineTag[] | null;
  tasks?: ITask[] | null;
  pipelineOwner?: IUser | null;
  customer?: ICustomer | null;
  stageOfPipeline?: IMasterStaticType | null;
  subPipeline?: ISubPipeline | null;
}

export const defaultValue: Readonly<IPipeline> = {};
