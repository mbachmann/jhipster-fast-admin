import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DescriptiveDocumentTextFaDetailComponent } from './descriptive-document-text-fa-detail.component';

describe('Component Tests', () => {
  describe('DescriptiveDocumentTextFa Management Detail Component', () => {
    let comp: DescriptiveDocumentTextFaDetailComponent;
    let fixture: ComponentFixture<DescriptiveDocumentTextFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DescriptiveDocumentTextFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ descriptiveDocumentText: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DescriptiveDocumentTextFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DescriptiveDocumentTextFaDetailComponent);
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
