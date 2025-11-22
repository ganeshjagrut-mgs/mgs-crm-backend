import { ISubPipelineOpenStage } from 'app/shared/model/sub-pipeline-open-stage.model';
import { ISubPipelineCloseStage } from 'app/shared/model/sub-pipeline-close-stage.model';

export interface ISubPipeline {
  id?: number;
  name?: string;
  index?: number | null;
  openStages?: ISubPipelineOpenStage[] | null;
  closeStages?: ISubPipelineCloseStage[] | null;
}

export const defaultValue: Readonly<ISubPipeline> = {};
