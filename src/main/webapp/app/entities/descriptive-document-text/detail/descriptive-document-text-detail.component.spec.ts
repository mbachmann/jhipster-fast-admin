import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DescriptiveDocumentTextDetailComponent } from './descriptive-document-text-detail.component';

describe('Component Tests', () => {
  describe('DescriptiveDocumentText Management Detail Component', () => {
    let comp: DescriptiveDocumentTextDetailComponent;
    let fixture: ComponentFixture<DescriptiveDocumentTextDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DescriptiveDocumentTextDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ descriptiveDocumentText: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DescriptiveDocumentTextDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DescriptiveDocumentTextDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load descriptiveDocumentText on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.descriptiveDocumentText).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
