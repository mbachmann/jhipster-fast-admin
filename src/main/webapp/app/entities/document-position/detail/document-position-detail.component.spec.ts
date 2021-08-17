import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DocumentPositionDetailComponent } from './document-position-detail.component';

describe('Component Tests', () => {
  describe('DocumentPosition Management Detail Component', () => {
    let comp: DocumentPositionDetailComponent;
    let fixture: ComponentFixture<DocumentPositionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DocumentPositionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ documentPosition: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DocumentPositionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DocumentPositionDetailComponent);
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
