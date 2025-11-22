import { IPipeline } from 'app/shared/model/pipeline.model';

export interface IPipelineTag {
  id?: number;
  name?: string;
  pipeline?: IPipeline;
}

export const defaultValue: Readonly<IPipelineTag> = {};
