import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DocumentPositionFaDetailComponent } from './document-position-fa-detail.component';

describe('Component Tests', () => {
  describe('DocumentPositionFa Management Detail Component', () => {
    let comp: DocumentPositionFaDetailComponent;
    let fixture: ComponentFixture<DocumentPositionFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DocumentPositionFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ documentPosition: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DocumentPositionFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DocumentPositionFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load documentPosition on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.documentPosition).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
