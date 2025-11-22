import { IMasterStaticType } from 'app/shared/model/master-static-type.model';
import { ISubPipeline } from 'app/shared/model/sub-pipeline.model';

export interface ISubPipelineOpenStage {
  id?: number;
  index?: number | null;
  stage?: IMasterStaticType;
  subPipeline?: ISubPipeline;
}

export const defaultValue: Readonly<ISubPipelineOpenStage> = {};
